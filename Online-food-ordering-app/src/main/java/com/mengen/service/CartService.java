package com.mengen.service;

import com.mengen.model.Cart;
import com.mengen.model.CartItem;
import com.mengen.request.AddCartItemRequestDTO;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequestDTO request, String jwt) throws Exception;
    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    Cart removeItemFromCart(Long cartItemId, String jwt)throws Exception;

    Long calculateCartTotals(Cart cart);

    Cart findCartById(Long id) throws Exception;

    Cart findCartByUserId(Long userId) throws Exception;

    Cart clearCart(Long userId) throws Exception;
}
