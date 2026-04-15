package com.uade.tpo.demo.service.favorite;


import java.util.List;

import com.uade.tpo.demo.entity.Favorite;

public interface FavoriteService {
    void addFavorite(Long userId, Long productId);
    void removeFavorite(Long userId, Long productId);
    List<Favorite> getFavorites(Long userId);
}