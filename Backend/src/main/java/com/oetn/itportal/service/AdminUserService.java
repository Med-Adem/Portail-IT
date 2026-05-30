package com.oetn.itportal.service;

import com.oetn.itportal.dto.AdminUserDto;
import com.oetn.itportal.model.User;
import com.oetn.itportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AdminUserDto.Response> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AdminUserDto.Response updateUser(Long id, AdminUserDto.UpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + id));

        // Prevent demoting yourself
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getUsername().equals(currentUsername) && request.getRole() == User.Role.USER) {
            throw new RuntimeException("Vous ne pouvez pas vous rétrograder vous-même.");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        // Recalculate username from new names
        String newUsername = (request.getFirstName() + "." + request.getLastName()).toLowerCase();
        // Only update username if it doesn't conflict with another user
        if (!newUsername.equals(user.getUsername()) && !userRepository.existsByUsername(newUsername)) {
            user.setUsername(newUsername);
        }

        // Update password only if provided
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getNewPassword().length() < 6) {
                throw new RuntimeException("Le nouveau mot de passe doit avoir au moins 6 caractères.");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + id));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("Vous ne pouvez pas supprimer votre propre compte.");
        }

        userRepository.deleteById(id);
    }

    private AdminUserDto.Response mapToResponse(User user) {
        return AdminUserDto.Response.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
