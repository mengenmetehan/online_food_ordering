package com.mengen.service;

import com.mengen.exceptions.RestaurantNotFoundException;
import com.mengen.exceptions.UserNotFoundException;
import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.request.RestaurantRequestDTO;
import com.mengen.request.UserRequestDTO;
import com.mengen.response.RestaurantDTO;
import com.mengen.response.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantService {

    Restaurant createRestaurant(RestaurantRequestDTO restaurantDTO, UserRequestDTO userRequestDTO) throws UserNotFoundException;

    Restaurant createRestaurant(RestaurantRequestDTO restaurantDTO, User user) throws UserNotFoundException;

    Restaurant updateRestaurant(Long id, RestaurantRequestDTO restaurantDTO) throws Exception;

    void deleteRestaurant(Long id) throws Exception;

    List<RestaurantResponseDTO> getAllRestaurants();

    List<RestaurantResponseDTO> searchRestaurants(String query);


    RestaurantResponseDTO findRestaurantById(Long id) throws RestaurantNotFoundException;

    RestaurantResponseDTO addToFavorites(Long id, UserRequestDTO userRequestDTO) throws Exception;

    RestaurantResponseDTO updateRestaurantStatus(Long id) throws Exception;

    RestaurantResponseDTO removeFromFavorites(Long id, UserRequestDTO userRequestDTO) throws Exception;

    RestaurantResponseDTO findByOwnerId(Long id) throws RestaurantNotFoundException;

    RestaurantResponseDTO findByOwnerEmail(String email) throws RestaurantNotFoundException;




}
