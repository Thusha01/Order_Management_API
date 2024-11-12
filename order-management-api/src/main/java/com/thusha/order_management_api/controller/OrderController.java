package com.thusha.order_management_api.controller;

import com.thusha.order_management_api.config.JwtConfig;
import com.thusha.order_management_api.dto.PlaceOrderDto;
import com.thusha.order_management_api.model.Client;
import com.thusha.order_management_api.model.Order;
import com.thusha.order_management_api.model.OrderStatus;
import com.thusha.order_management_api.service.ClientService;
import com.thusha.order_management_api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private JwtConfig jwtConfig;

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderDto orderRequest, Authentication authentication) {
        String email = authentication.getName();
        Optional<Client> clientOptional= clientService.getClientByEmail(email);

        if (clientOptional.isEmpty()){
            return ResponseEntity.status(404).body("Client not found");
        }

        Client client = clientOptional.get();
        Order order= orderService.placeOrder(client, orderRequest);

        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/cancel/{referenceNumber}")
    public ResponseEntity<?> cancelOrder(@PathVariable String referenceNumber, Authentication authentication) {
        String email = authentication.getName();
        Optional<Client> clientOptional = clientService.getClientByEmail(email);

        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Client not found");
        }

        Client client = clientOptional.get();
        Optional<Order> orderOptional = orderService.findOrderByReferenceNUmber(referenceNumber, client);

        if (orderOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Order not found");
        }

        Order order = orderOptional.get();


        if (OrderStatus.NEW.equals(order.getStatus())) {
            orderService.cancelOrder(order);
            return ResponseEntity.ok("Order canceled successfully");
        }


        String currentStatus = order.getStatus().name();
        return ResponseEntity.status(403).body("Order cannot be canceled. It is already " + currentStatus);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/history")
    public ResponseEntity<?> getOrderHistory(@RequestParam int page, @RequestParam int size, Authentication authentication) {

        System.out.println("Authentication details: " + authentication);

        String email = authentication.getName();
        Optional<Client> clientOptional = clientService.getClientByEmail(email);

        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Client not found");
        }

        Client client = clientOptional.get();
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getOrderHistory(client, pageable);

        if (orders.isEmpty()) {
            return ResponseEntity.ok("No order history available.");
        }

        return ResponseEntity.ok(orders);
    }

}
