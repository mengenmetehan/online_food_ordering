package com.mengen.service.impl;

import com.mengen.model.*;
import com.mengen.repository.AddressRepository;
import com.mengen.repository.OrderItemRepository;
import com.mengen.repository.OrderRepository;
import com.mengen.repository.UserRepository;
import com.mengen.request.OrderRequestDTO;
import com.mengen.service.CartService;
import com.mengen.service.OrderService;
import com.mengen.service.RestaurantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;


    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    private static final List CANCELLABLE_ORDER_STATUS = List.of("COMPLETED", "DELIVERED", "OUT_FOR_DELIVERY", "PENDING");

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Order createOrder(OrderRequestDTO request, User user) throws Exception {
        Address deliveryAddress = request.getDeliveryAddress();
        Address savedAddress = addressRepository.save(deliveryAddress);

        if (!user.getAddresses().contains(savedAddress)) {
            user.getAddresses().add(savedAddress);
            user = userRepository.save(user);
        }

        Restaurant restaurantById = restaurantService.findRestaurantById(request.getRestaurantId());
        Order order = new Order();
        order.setCustomer(user);
        order.setRestaurant(restaurantById);
        order.setCreatedAt(new Date());
        order.setDeliveryAddress(savedAddress);
        order.setDeliveryAddress(savedAddress);

        Cart cartByUserId = cartService.findCartByUserId(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartByUserId.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity((long)cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setIngredients(cartItem.getIngredients());

            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

        Long totalPrice = cartService.calculateCartTotals(cartByUserId);

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        restaurantById.getOrders().add(savedOrder);

        return savedOrder;
    }

    @Override
    public Order updateOrder(Long id, String orderStatus) throws Exception {
        Order orderById = findOrderById(id);

        if (CANCELLABLE_ORDER_STATUS.contains(orderStatus)) {
            orderById.setOrderStatus(orderStatus);
            return orderRepository.save(orderById);
        }
        throw new Exception("%s : orderId cannot be cancelled due to status : %s".formatted(id, orderStatus));
    }

    @Override
    public void cancelOrder(Long id) throws Exception {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getUserOrders(Long id) throws Exception {
        return null;
    }

    @Override
    public List<Order> getRestaurantOrder(Long id, String orderStatus) throws Exception {
        List<Order> orders = orderRepository.findByRestaurantId(id);

        if (StringUtils.isEmpty(orderStatus)) {
            return orders;
        }

        return orders.stream()
                .filter(order -> order.getOrderStatus().equals(orderStatus)).toList();
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found"));
    }
}
