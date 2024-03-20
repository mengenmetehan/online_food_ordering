package com.mengen.controller;

import com.mengen.model.Food;
import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.request.FoodRequestDTO;
import com.mengen.service.FoodService;
import com.mengen.service.RestaurantService;
import com.mengen.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;


    public AdminFoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<Food> createFood(FoodRequestDTO foodRequestDTO,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Restaurant restaurantById = restaurantService.findRestaurantById(foodRequestDTO.getRestaurantId());


        return new ResponseEntity<>(foodService.createFood(foodRequestDTO, foodRequestDTO.getCategory(), restaurantById), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable("id") Long id,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id);
        return ResponseEntity.ok("Food deleted successfully");
    }

    @PostMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailbilityStatus(@PathVariable("id") Long id,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Food food = foodService.updateAvailbilityStatus(id);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
}
