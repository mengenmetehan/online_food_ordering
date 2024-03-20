package com.mengen.service;

import com.mengen.exceptions.FoodException;
import com.mengen.model.Category;
import com.mengen.model.Food;
import com.mengen.model.Restaurant;
import com.mengen.request.FoodRequestDTO;

import java.util.List;

public interface FoodService {

    Food createFood(FoodRequestDTO foodRequestDTO, Category category, Restaurant restaurant);
    void deleteFood(Long id) throws FoodException;
    List<Food> getRestaurantFoods(Long restaurantId, boolean isVegeterian, boolean isNonveg,
                                  boolean isSeasonal, String foodCategory);

    List<Food> searchFood(String query);
    Food findFoodById(Long id) throws FoodException;
    Food updateAvailbilityStatus(Long id) throws FoodException;

}
