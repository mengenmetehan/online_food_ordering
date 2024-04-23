package com.mengen.controller;


import com.mengen.model.CartItem;
import com.mengen.model.Order;
import com.mengen.model.User;
import com.mengen.request.AddCartItemRequestDTO;
import com.mengen.request.OrderRequestDTO;
import com.mengen.service.OrderService;
import com.mengen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO request,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Order order = orderService.createOrder(request, userByJwtToken);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestBody OrderRequestDTO request,
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        List<Order> userOrders = orderService.getUserOrders(userByJwtToken.getId());
        return ResponseEntity.ok(userOrders);
    }


}
