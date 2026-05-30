package com.oetn.itportal.controller;

import com.oetn.itportal.dto.SoftwareRequestDto;
import com.oetn.itportal.service.SoftwareRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/software-requests")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class SoftwareRequestController {

    @Autowired
    private SoftwareRequestService service;

    @PostMapping
    public ResponseEntity<SoftwareRequestDto.Response> create(@Valid @RequestBody SoftwareRequestDto.CreateRequest req) {
        return ResponseEntity.ok(service.createRequest(req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<SoftwareRequestDto.Response>> getMy() {
        return ResponseEntity.ok(service.getMyRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoftwareRequestDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SoftwareRequestDto.Response>> getAll() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SoftwareRequestDto.Response> updateStatus(@PathVariable Long id, @RequestBody SoftwareRequestDto.UpdateStatusRequest req) {
        return ResponseEntity.ok(service.updateStatus(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
