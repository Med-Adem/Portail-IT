package com.oetn.itportal.service;

import com.oetn.itportal.dto.SoftwareRequestDto;
import com.oetn.itportal.model.SoftwareRequest;
import com.oetn.itportal.model.User;
import com.oetn.itportal.repository.SoftwareRequestRepository;
import com.oetn.itportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoftwareRequestService {

    @Autowired private SoftwareRequestRepository softwareRequestRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TicketTraceCsvService ticketTraceCsvService;

    public SoftwareRequestDto.Response createRequest(SoftwareRequestDto.CreateRequest request) {
        User currentUser = getCurrentUser();

        SoftwareRequest req = SoftwareRequest.builder()
                .softwareName(request.getSoftwareName())
                .reason(request.getReason())
                .status(SoftwareRequest.Status.PENDING)
                .user(currentUser)
                .build();

        SoftwareRequest saved = softwareRequestRepository.save(req);

        ticketTraceCsvService.trace(
                "software_request",
                saved.getId(),
                "CREATION",
                "",
                saved.getStatus().name(),
                currentUser.getUsername(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getSoftwareName()
        );

        return mapToResponse(saved);
    }

    public List<SoftwareRequestDto.Response> getMyRequests() {
        return softwareRequestRepository.findByUserOrderByCreatedAtDesc(getCurrentUser())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<SoftwareRequestDto.Response> getAllRequests() {
        return softwareRequestRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // FIX #2: Added ownership check — USER can only see their own requests
    public SoftwareRequestDto.Response getById(Long id) {
        SoftwareRequest req = softwareRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable: " + id));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;

        if (!isAdmin && !req.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Accès refusé : cette demande ne vous appartient pas.");
        }

        return mapToResponse(req);
    }

    public SoftwareRequestDto.Response updateStatus(Long id, SoftwareRequestDto.UpdateStatusRequest request) {
        SoftwareRequest req = softwareRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable: " + id));

        String oldStatus = req.getStatus().name();
        req.setStatus(request.getStatus());

        SoftwareRequest saved = softwareRequestRepository.save(req);

        ticketTraceCsvService.trace(
                "software_request",
                saved.getId(),
                "CHANGEMENT_STATUT",
                oldStatus,
                saved.getStatus().name(),
                getCurrentUser().getUsername(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getSoftwareName()
        );

        return mapToResponse(saved);
    }

    public void deleteRequest(Long id) {
        if (!softwareRequestRepository.existsById(id)) throw new RuntimeException("Demande introuvable: " + id);
        softwareRequestRepository.deleteById(id);
    }

    private SoftwareRequestDto.Response mapToResponse(SoftwareRequest req) {
        return SoftwareRequestDto.Response.builder()
                .id(req.getId())
                .softwareName(req.getSoftwareName())
                .reason(req.getReason())
                .status(req.getStatus().name())
                .userEmail(req.getUser().getUsername())
                .username(req.getUser().getFullName())
                .createdAt(req.getCreatedAt())
                .updatedAt(req.getUpdatedAt())
                .build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    }
}
