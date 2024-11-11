package com.thusha.order_management_api.dto;

import lombok.Data;

@Data
public class PlaceOrderDto {
    private String itemName;
    private int quantity;
    private String shippingAddress;
}
