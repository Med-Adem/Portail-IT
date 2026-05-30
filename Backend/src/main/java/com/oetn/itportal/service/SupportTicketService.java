package com.oetn.itportal.service;

import com.oetn.itportal.dto.SupportTicketDto;
import com.oetn.itportal.model.SupportTicket;
import com.oetn.itportal.model.User;
import com.oetn.itportal.repository.SupportTicketRepository;
import com.oetn.itportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketTraceCsvService ticketTraceCsvService;

    public SupportTicketDto.Response createTicket(SupportTicketDto.CreateRequest request) {
        User currentUser = getCurrentUser();

        SupportTicket ticket = SupportTicket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(SupportTicket.Status.OPEN)
                .user(currentUser)
                .build();

        SupportTicket saved = ticketRepository.save(ticket);

        ticketTraceCsvService.trace(
                "support_ticket",
                saved.getId(),
                "CREATION",
                "",
                saved.getStatus().name(),
                currentUser.getUsername(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getTitle());

        return mapToResponse(saved);
    }

    public List<SupportTicketDto.Response> getMyTickets() {
        return ticketRepository.findByUserOrderByCreatedAtDesc(getCurrentUser())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<SupportTicketDto.Response> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // FIX #2: Added ownership check — USER can only see their own tickets
    public SupportTicketDto.Response getTicketById(Long id) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable: " + id));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;

        if (!isAdmin && !ticket.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Accès refusé : ce ticket ne vous appartient pas.");
        }

        return mapToResponse(ticket);
    }

    public SupportTicketDto.Response updateStatus(Long id, SupportTicketDto.UpdateStatusRequest request) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable: " + id));

        String oldStatus = ticket.getStatus().name();
        ticket.setStatus(request.getStatus());

        SupportTicket saved = ticketRepository.save(ticket);

        ticketTraceCsvService.trace(
                "support_ticket",
                saved.getId(),
                "CHANGEMENT_STATUT",
                oldStatus,
                saved.getStatus().name(),
                getCurrentUser().getUsername(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getTitle());

        return mapToResponse(saved);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id))
            throw new RuntimeException("Ticket introuvable: " + id);
        ticketRepository.deleteById(id);
    }

    private SupportTicketDto.Response mapToResponse(SupportTicket ticket) {
        return SupportTicketDto.Response.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus().name())
                .userEmail(ticket.getUser().getUsername())
                .username(ticket.getUser().getFullName())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    }
}
