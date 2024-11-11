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
import org.springframework.web.bind.annotation.*;
import com.thusha.order_management_api.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthService authService;

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
    public ResponseEntity<AuthResponseDto> authenticateUser(@RequestBody LoginDto loginRequest) {
        // Use AuthService to authenticate and generate the JWT
        AuthResponseDto authResponse = authService.signin(loginRequest);
        return ResponseEntity.ok(authResponse);
    }
}
