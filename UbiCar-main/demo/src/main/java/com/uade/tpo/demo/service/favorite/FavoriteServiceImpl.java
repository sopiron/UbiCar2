package com.uade.tpo.demo.service.favorite;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Favorite;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.cart.ProductNotFoundException;
import com.uade.tpo.demo.exceptions.cart.UserNotFoundException;
import com.uade.tpo.demo.exceptions.favoritos.FavoriteAlreadyExistsException;
import com.uade.tpo.demo.exceptions.favoritos.FavoriteNotFoundException;
import com.uade.tpo.demo.repository.FavoriteRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.UserRepository;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addFavorite(Long userId, Long productId) {

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        if (favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new FavoriteAlreadyExistsException();
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {

        Favorite favorite = favoriteRepository
                .findByUserIdAndProductId(userId, productId)
                .orElseThrow(FavoriteNotFoundException::new);

        favoriteRepository.delete(favorite);
    }

    @Override
    public List<Favorite> getFavorites(Long userId) {
            userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return favoriteRepository.findByUserId(userId);

    }
}
