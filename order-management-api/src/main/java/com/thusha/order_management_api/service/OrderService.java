package com.thusha.order_management_api.service;

import com.thusha.order_management_api.dto.PlaceOrderDto;
import com.thusha.order_management_api.model.Client;
import com.thusha.order_management_api.model.Order;
import com.thusha.order_management_api.model.OrderStatus;
import com.thusha.order_management_api.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
     @Autowired
    private OrderRepository orderRepository;

     @Transactional
    public Order placeOrder(Client client, PlaceOrderDto orderRequest) {
         Order order = new Order();
         order.setReferenceNumber(generateReferenceNumber());
         order.setItemName(orderRequest.getItemName());
         order.setQuantity(orderRequest.getQuantity());
         order.setShippingAddress(orderRequest.getShippingAddress());
         order.setStatus(OrderStatus.NEW);
         order.setCreatedAt(LocalDateTime.now());
         order.setClient(client);

         return orderRepository.save(order);
     }



     @Transactional
    public void cancelOrder(Order order) {
         order.setStatus(OrderStatus.CANCELED);
         orderRepository.save(order);
     }


    public Optional<Order> findOrderByReferenceNUmber(String referenceNumber, Client client) {
        return orderRepository.findByReferenceNumberAndClient(referenceNumber, client);
    }

     public Page<Order> getOrderHistory(Client client, Pageable pageable) {
         return (Page<Order>) orderRepository.findByClient(client, pageable);
     }


     private String generateReferenceNumber() {
         return "ORD-" + System.currentTimeMillis();
     }


}
