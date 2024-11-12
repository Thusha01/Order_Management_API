package com.thusha.order_management_api.service;

import com.thusha.order_management_api.model.Client;
import com.thusha.order_management_api.repository.ClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final ClientRepository clientRepository;

    public UserDetailsServiceImp(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(
                client.getEmail(), client.getPassword(), client.getAuthorities());
    }
}
