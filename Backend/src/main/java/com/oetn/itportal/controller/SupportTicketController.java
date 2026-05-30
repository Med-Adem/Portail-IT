package com.oetn.itportal.controller;

import com.oetn.itportal.dto.SupportTicketDto;
import com.oetn.itportal.service.SupportTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class SupportTicketController {

    @Autowired
    private SupportTicketService ticketService;

    @PostMapping
    public ResponseEntity<SupportTicketDto.Response> create(@Valid @RequestBody SupportTicketDto.CreateRequest req) {
        return ResponseEntity.ok(ticketService.createTicket(req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<SupportTicketDto.Response>> getMyTickets() {
        return ResponseEntity.ok(ticketService.getMyTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupportTicketDto.Response>> getAll() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupportTicketDto.Response> updateStatus(@PathVariable Long id, @RequestBody SupportTicketDto.UpdateStatusRequest req) {
        return ResponseEntity.ok(ticketService.updateStatus(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
