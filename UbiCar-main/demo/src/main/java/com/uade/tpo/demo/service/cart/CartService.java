package com.uade.tpo.demo.service.cart;

import com.uade.tpo.demo.controllers.cart.CartItemRequest;
import com.uade.tpo.demo.controllers.cart.CartResponse;
import com.uade.tpo.demo.entity.Cart;
public interface CartService {

    public CartResponse getCart();
    
    public CartResponse addProduct(CartItemRequest request);

    public CartResponse modifyCart(CartItemRequest request);
  
    public CartResponse removeItem(Long itemId);
    
    public Cart confirmCart();
}