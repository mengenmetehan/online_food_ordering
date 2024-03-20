package com.mengen.service;

import com.mengen.exceptions.RestaurantNotFoundException;
import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.request.RestaurantRequestDTO;
import com.mengen.request.UserRequestDTO;
import com.mengen.response.RestaurantDTO;

import java.util.List;

public interface RestaurantService {

    Restaurant createRestaurant(RestaurantRequestDTO restaurantDTO, User user);


    Restaurant updateRestaurant(Long id, RestaurantRequestDTO restaurantDTO) throws Exception;

    void deleteRestaurant(Long id) throws Exception;

    List<Restaurant> getAllRestaurants();

    List<Restaurant> searchRestaurants(String query);


    Restaurant findRestaurantById(Long id) throws RestaurantNotFoundException;

    RestaurantDTO addToFavorites(Long id, User user) throws Exception;

    Restaurant updateRestaurantStatus(Long id) throws Exception;

    Restaurant getRestaurantByUserId(Long id) throws RestaurantNotFoundException;

    Restaurant removeFromFavorites(Long id, UserRequestDTO userRequestDTO) throws Exception;

    Restaurant getRestaurantByEmail(String email) throws RestaurantNotFoundException;




}
