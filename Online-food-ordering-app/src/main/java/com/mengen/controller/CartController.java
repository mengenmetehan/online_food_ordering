package com.mengen.controller;

import com.mengen.model.Cart;
import com.mengen.model.CartItem;
import com.mengen.model.User;
import com.mengen.request.AddCartItemRequestDTO;
import com.mengen.request.UpdateCartRequestDTO;
import com.mengen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mengen.service.CartService;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequestDTO request,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        CartItem cartItem = cartService.addItemToCart(request, jwt);
        return ResponseEntity.ok(cartItem);
    }


    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(@RequestBody UpdateCartRequestDTO request,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {
        CartItem cartItem = cartService.updateCartItemQuantity(request.getCartItemId(), request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> deleteCartItem(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        Cart cart = cartService.removeItemFromCart(id, jwt);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart( @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.clearCart(userByJwtToken.getId());
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart( @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findCartByUserId(userByJwtToken.getId());
        return ResponseEntity.ok(cart);
    }

}
