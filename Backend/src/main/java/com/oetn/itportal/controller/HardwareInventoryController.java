package com.oetn.itportal.controller;

import com.oetn.itportal.dto.HardwareDeviceDto;
import com.oetn.itportal.service.HardwareInventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hardware")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class HardwareInventoryController {

    @Autowired
    private HardwareInventoryService service;

    @PostMapping
    public ResponseEntity<HardwareDeviceDto.Response> submit(@Valid @RequestBody HardwareDeviceDto.CreateRequest req) {
        return ResponseEntity.ok(service.submitDevice(req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<HardwareDeviceDto.Response>> getMy() {
        return ResponseEntity.ok(service.getMyDevices());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HardwareDeviceDto.Response>> getAll() {
        return ResponseEntity.ok(service.getAllDevices());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
