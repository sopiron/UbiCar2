package com.uade.tpo.demo.service.cart;

import com.uade.tpo.demo.controllers.cart.CartItemRequest;
import com.uade.tpo.demo.entity.Cart;
public interface CartService {

    public Cart getCart();
    
    public Cart addProduct(CartItemRequest request);

    public Cart modifyCart(CartItemRequest request);
  
    public Cart confirmCart();
}