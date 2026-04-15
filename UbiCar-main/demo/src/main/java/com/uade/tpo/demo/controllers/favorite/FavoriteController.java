package com.uade.tpo.demo.controllers.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Favorite;
import com.uade.tpo.demo.service.favorite.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public String addFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        favoriteService.addFavorite(userId, productId);
        return "added";
    }

    @DeleteMapping("/remove")
    public String removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return "removed";
    }

    @GetMapping("/list")
    public List<Long> getFavorites(@RequestParam Long userId) {
        List<Favorite> favorites = favoriteService.getFavorites(userId); //lista de entidades favorite

        List<Long> productIds = favorites.stream() //transforma cada Favorite en un Long (productId) 
            .map(favorite -> favorite.getProduct().getId()).toList(); 

        return productIds; 
    }
}
