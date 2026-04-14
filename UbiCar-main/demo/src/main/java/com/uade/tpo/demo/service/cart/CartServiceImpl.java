package com.uade.tpo.demo.service.cart;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.cart.CartItemRequest;
import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.CartItemRepository;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;

import com.uade.tpo.demo.exceptions.cart.CartNotFoundException;
import com.uade.tpo.demo.exceptions.cart.EmptyCartException;
import com.uade.tpo.demo.exceptions.cart.ProductInCartException;
import com.uade.tpo.demo.exceptions.cart.ProductNotFoundException;
import com.uade.tpo.demo.exceptions.cart.ProductNotInCartException;
import com.uade.tpo.demo.exceptions.cart.UserNotFoundException;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // obtener usuario logueado
    private User getUserFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    // obtener carrito actual
    @Override
    public Cart getCart() {

        User user = getUserFromToken();

        return cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);
    }

    // agregar producto al carrito
    @Override
    public Cart addProduct(CartItemRequest request) {

        User user = getUserFromToken();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        // buscar si ya existe el mismo producto con la misma fecha
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId())
                        && item.getDate().equals(request.getDate()))
                .findFirst();

        if (existingItem.isPresent()) {
            throw new ProductInCartException();
        }

        // crear nuevo item
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setDate(request.getDate());

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        cart.getItems().add(item);

        return cartRepository.save(cart);
    }

    // modificar fecha de un producto en el carrito
    @Override
    public Cart modifyCart(CartItemRequest request) {

        User user = getUserFromToken();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(request.getProductId())) {
                item.setDate(request.getDate());
                return cartRepository.save(cart);
            }
        }

        throw new ProductNotInCartException();
    }

    // confirmar compra y vaciar carrito
    @Override
    public Cart confirmCart() {

        User user = getUserFromToken();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        cart.getItems().clear();

        return cartRepository.save(cart);
    }
}