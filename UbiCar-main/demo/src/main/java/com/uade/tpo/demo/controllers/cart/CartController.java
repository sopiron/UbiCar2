package com.uade.tpo.demo.controllers.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.service.cart.CartService;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Obtener el carrito actual
   @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    // Agregar un producto al carrito
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProduct(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addProduct(request));
    }

    // Modificar fecha en el carrito
    @PutMapping
    public ResponseEntity<CartResponse> modifyCart(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.modifyCart(request));
    }

    //eliminar producto del carrito
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(itemId));
    }

    // Confirmar la compra
    @PostMapping("/confirm")
    public ResponseEntity<Cart> confirmCart() {
        return ResponseEntity.ok(cartService.confirmCart());
    }
    
}