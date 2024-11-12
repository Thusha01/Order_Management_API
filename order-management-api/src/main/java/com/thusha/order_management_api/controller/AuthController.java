package com.thusha.order_management_api.controller;

import com.thusha.order_management_api.dto.AuthResponseDto;
import com.thusha.order_management_api.dto.LoginDto;
import com.thusha.order_management_api.dto.SignupDto;
import com.thusha.order_management_api.config.JwtConfig;
import com.thusha.order_management_api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDto signupRequest) {
        try {
            clientService.registerClient(signupRequest);
            return ResponseEntity.ok("User registered successfully!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @PostMapping("/signin")
    public AuthResponseDto AuthenticateAndGetToken(@RequestBody LoginDto authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return AuthResponseDto.builder()
                    .token(jwtConfig.generateToken(authRequestDTO.getEmail()))
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }

}
