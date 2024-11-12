package com.thusha.order_management_api.service;

import com.thusha.order_management_api.config.JwtConfig;
import com.thusha.order_management_api.dto.AuthResponseDto;
import com.thusha.order_management_api.dto.LoginDto;
import com.thusha.order_management_api.dto.SignupDto;
import com.thusha.order_management_api.model.Client;
import com.thusha.order_management_api.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    // Modify the method to accept LoginDto instead of SignupDto
    public AuthResponseDto signin(LoginDto request) {
        // Find the client by email
        Client client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check if the password matches the stored password hash
        if (!passwordEncoder.matches(request.getPassword(), client.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate the JWT token
        String token = jwtConfig.generateToken(client);
        return new AuthResponseDto(token);  // Return the token in a response DTO
    }

}
