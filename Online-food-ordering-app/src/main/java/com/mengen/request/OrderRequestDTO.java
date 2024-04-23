package com.mengen.request;

import com.mengen.model.Address;
import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long restaurantId;
    private Address deliveryAddress;
}
