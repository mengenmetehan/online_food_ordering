package com.mengen.controller;

import com.mengen.model.Order;
import com.mengen.model.User;
import com.mengen.request.OrderRequestDTO;
import com.mengen.service.OrderService;
import com.mengen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    private final OrderService orderService;
    private final UserService userService;

    public AdminOrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    @GetMapping("/order/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long id,
                                                       @RequestParam String orderStatus,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        List<Order> userOrders = orderService.getRestaurantOrder(id,orderStatus);
        return ResponseEntity.ok(userOrders);
    }

    @PutMapping("/order/{id}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                       @PathVariable String orderStatus,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Order order = orderService.updateOrder(id, orderStatus);
        return ResponseEntity.ok(order);
    }
}
