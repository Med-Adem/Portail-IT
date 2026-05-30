package com.oetn.itportal.service;

import com.oetn.itportal.dto.HardwareDeviceDto;
import com.oetn.itportal.model.HardwareDevice;
import com.oetn.itportal.model.User;
import com.oetn.itportal.repository.HardwareDeviceRepository;
import com.oetn.itportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class HardwareInventoryService {

    @Autowired private HardwareDeviceRepository hardwareDeviceRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TicketTraceCsvService ticketTraceCsvService;

    public HardwareDeviceDto.Response submitDevice(HardwareDeviceDto.CreateRequest request) {
        User currentUser = getCurrentUser();

        if (hardwareDeviceRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new RuntimeException("Ce numéro de série est déjà enregistré.");
        }

        HardwareDevice device = HardwareDevice.builder()
                .username(currentUser.getUsername())
                .laptopModel(request.getLaptopModel())
                .serialNumber(request.getSerialNumber())
                .user(currentUser)
                .build();

        HardwareDevice saved = hardwareDeviceRepository.save(device);

        ticketTraceCsvService.trace(
                "hardware_inventory",
                saved.getId(),
                "SOUMISSION",
                "",
                "SUBMITTED",
                currentUser.getUsername(),
                saved.getSubmittedAt(),
                LocalDateTime.now(),
                saved.getSerialNumber()
        );

        return mapToResponse(saved);
    }

    public List<HardwareDeviceDto.Response> getMyDevices() {
        return hardwareDeviceRepository.findByUserOrderBySubmittedAtDesc(getCurrentUser())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<HardwareDeviceDto.Response> getAllDevices() {
        return hardwareDeviceRepository.findAllByOrderBySubmittedAtDesc()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public void deleteDevice(Long id) {
        if (!hardwareDeviceRepository.existsById(id)) throw new RuntimeException("Appareil introuvable: " + id);
        hardwareDeviceRepository.deleteById(id);
    }

    private HardwareDeviceDto.Response mapToResponse(HardwareDevice device) {
        return HardwareDeviceDto.Response.builder()
                .id(device.getId())
                .username(device.getUsername())
                .laptopModel(device.getLaptopModel())
                .serialNumber(device.getSerialNumber())
                .userEmail(device.getUser().getUsername())   // username remplace email
                .submittedAt(device.getSubmittedAt())
                .build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    }
}
