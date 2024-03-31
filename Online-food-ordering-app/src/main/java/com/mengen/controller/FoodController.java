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

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;


    public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFoods(@RequestParam boolean vegetarian,
                                                         @RequestParam boolean seasonal,
                                                         @RequestParam boolean nonveg,
                                                         @PathVariable Long restaurantId,
                                                         @RequestParam (required = false) String foodCategory,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        List<Food> foods = foodService.getRestaurantFoods(restaurantId, vegetarian, nonveg, seasonal, foodCategory);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
