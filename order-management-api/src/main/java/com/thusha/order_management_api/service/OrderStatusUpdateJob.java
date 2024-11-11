package com.thusha.order_management_api.service;

import com.thusha.order_management_api.model.Order;
import com.thusha.order_management_api.model.OrderStatus;
import com.thusha.order_management_api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusUpdateJob {

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateOrderStatus(){
        List<Order> newOrders = orderRepository.findAllByStatus("NEW");
        for(Order order : newOrders){
            order.setStatus(OrderStatus.DISPATCHED);
            orderRepository.save(order);
        }
    }
}
