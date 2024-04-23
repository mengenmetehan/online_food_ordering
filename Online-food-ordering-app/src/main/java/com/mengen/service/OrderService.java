package com.mengen.service;

import com.mengen.model.User;
import com.mengen.request.OrderRequestDTO;
import com.mengen.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequestDTO request, User user) throws Exception;

    Order updateOrder(Long id, String orderStatus) throws Exception;

    void cancelOrder(Long id) throws Exception;

    List<Order> getUserOrders(Long id) throws Exception;

    List<Order> getRestaurantOrder(Long id, String orderStatus) throws Exception;

    Order findOrderById(Long id) throws Exception;
}
