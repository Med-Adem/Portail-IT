package com.oetn.itportal.service;

import com.oetn.itportal.dto.AuthResponse;
import com.oetn.itportal.dto.LoginRequest;
import com.oetn.itportal.dto.RegisterRequest;
import com.oetn.itportal.model.User;
import com.oetn.itportal.repository.UserRepository;
import com.oetn.itportal.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        String username = (request.getFirstName() + "." + request.getLastName()).toLowerCase();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Un compte avec ce nom existe déjà : " + username);
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(username)
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername().toLowerCase(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Nom d'utilisateur ou mot de passe incorrect.");
        }

        User user = userRepository.findByUsername(request.getUsername().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}
