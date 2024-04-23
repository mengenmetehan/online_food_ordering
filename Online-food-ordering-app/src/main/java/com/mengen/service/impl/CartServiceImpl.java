package com.mengen.service.impl;

import com.mengen.model.Cart;
import com.mengen.model.CartItem;
import com.mengen.model.Food;
import com.mengen.model.User;
import com.mengen.repository.CartItemRepository;
import com.mengen.repository.CartRepository;
import com.mengen.request.AddCartItemRequestDTO;
import com.mengen.service.CartService;
import com.mengen.service.FoodService;
import com.mengen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

    private final CartItemRepository cartItemRepository;

    private final FoodService foodService;

    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository, UserService userService, CartItemRepository cartItemRepository, FoodService foodService, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
        this.foodService = foodService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItem addItemToCart(AddCartItemRequestDTO request, String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Food foodById = foodService.findFoodById(request.getFoodId());
        Cart cartByCurrentUser = cartRepository.findByCustomerId(userByJwtToken.getId());
        CartItem newCartItem = new CartItem();

        for (CartItem cartItem : cartByCurrentUser.getItems()) {
            if (cartItem.getFood().getId().equals(foodById.getId())) {
                int newQuantity = cartItem.getQuantity() + request.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        modelMapper.map(request, newCartItem);
        newCartItem.setTotalPrice(request.getQuantity() * foodById.getPrice());
        CartItem savedCart = cartItemRepository.save(newCartItem);
        cartByCurrentUser.getItems().add(savedCart);
        return savedCart;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * cartItem.getFood().getPrice());
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Cart cartByCurrentUser = cartRepository.findByCustomerId(userByJwtToken.getId());

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                        .orElseThrow(() -> new Exception("Cart item not found"));

        cartByCurrentUser.getItems().remove(cartItem);
        return cartRepository.save(cartByCurrentUser);
    }

    @Override
    public Long calculateCartTotals(Cart cart) {
        return cart.getItems().stream().mapToLong(CartItem::getTotalPrice).reduce(0, Long::sum);
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        return cartRepository.findById(id).orElseThrow(() -> new Exception("Cart item not found"));
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);
        cart.setTotal(calculateCartTotals(cart));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
