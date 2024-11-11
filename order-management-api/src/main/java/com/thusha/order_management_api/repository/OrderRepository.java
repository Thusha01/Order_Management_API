package com.thusha.order_management_api.repository;

import com.thusha.order_management_api.model.Order;
import com.thusha.order_management_api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByClient(Client client, Pageable pageable);
    Optional<Order> findByReferenceNumberAndClient(String referenceNumber, Client client);

    List<Order> findAllByStatus(String status);
}
