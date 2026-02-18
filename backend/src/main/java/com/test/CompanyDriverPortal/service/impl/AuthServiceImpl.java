package com.test.CompanyDriverPortal.service.impl;

import com.test.CompanyDriverPortal.config.JwtUtil;
import com.test.CompanyDriverPortal.dto.auth.LoginRequest;
import com.test.CompanyDriverPortal.dto.auth.LoginResponse;
import com.test.CompanyDriverPortal.dto.auth.RegisterRequest;
import com.test.CompanyDriverPortal.model.User;
import com.test.CompanyDriverPortal.repository.UserRepository;
import com.test.CompanyDriverPortal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Set role - default to USER if not provided or invalid
        User.Role userRole = User.Role.USER; // Default role

        if(registerRequest.getRole() != null && !registerRequest.getRole().isBlank()) {
            userRole = User.Role.valueOf(registerRequest.getRole().toUpperCase());
        }

        user.setRole(userRole);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        // Generate JWT token for the newly registered user
        String token = jwtUtil.generateToken(
                savedUser.getUsername(),
                savedUser.getRole().name());

        return new LoginResponse(
                token,
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                "User registered successfully"
        );
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        // Spring Security handles invalid username/password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + loginRequest.getUsername()
                        )
                );

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                "Login successful"
        );
    }

}