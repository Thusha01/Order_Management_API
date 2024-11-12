package com.thusha.order_management_api.service;

import com.thusha.order_management_api.config.JwtConfig;
import com.thusha.order_management_api.dto.SignupDto;
import com.thusha.order_management_api.model.Client;
import com.thusha.order_management_api.model.Role;
import com.thusha.order_management_api.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;


    @Autowired
    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public void registerClient(SignupDto request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        Client client = new Client();
        client.setEmail(request.getEmail());
        client.setPasswordHash(hashPassword);
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setRole(Role.CLIENT);

        clientRepository.save(client);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
