package com.thusha.order_management_api.repository;

import com.thusha.order_management_api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository  extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);

}
