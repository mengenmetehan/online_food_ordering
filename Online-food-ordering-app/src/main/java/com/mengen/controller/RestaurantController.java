package com.mengen.controller;

import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.request.RestaurantRequestDTO;
import com.mengen.request.UserRequestDTO;
import com.mengen.response.RestaurantDTO;
import com.mengen.response.RestaurantResponseDTO;
import com.mengen.service.RestaurantService;
import com.mengen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public RestaurantController(RestaurantService restaurantService, UserService userService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(@RequestParam String keyword,
                                                                        @RequestHeader("Authorization") String jwt) throws Exception
    {
        //User userByJwtToken = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurantResponseDTOS = restaurantService.searchRestaurants(keyword);

        return new ResponseEntity<>(restaurantResponseDTOS, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurants(@RequestHeader("Authorization") String jwt) throws Exception
    {
        List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(allRestaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
        public ResponseEntity<Restaurant> findRestaurantById(@PathVariable String id,
                                                                              @RequestHeader("Authorization") String jwt) throws Exception
    {
        Restaurant restaurantById = restaurantService.findRestaurantById(Long.valueOf(id));
        return new ResponseEntity<>(restaurantById, HttpStatus.OK);
    }

    @PutMapping("/{id}/add-favorites")
    public ResponseEntity<RestaurantDTO> addToFavorites(@PathVariable String id,
                                                        @RequestHeader("Authorization") String jwt) throws Exception
    {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        RestaurantDTO restaurant = restaurantService.addToFavorites(Long.valueOf(id), userByJwtToken);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }






















}
