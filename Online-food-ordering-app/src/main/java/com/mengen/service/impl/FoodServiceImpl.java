package com.mengen.service.impl;

import com.mengen.exceptions.FoodException;
import com.mengen.model.Category;
import com.mengen.model.Food;
import com.mengen.model.Restaurant;
import com.mengen.repository.FoodRepository;
import com.mengen.request.FoodRequestDTO;
import com.mengen.service.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final ModelMapper modelMapper;

    public FoodServiceImpl(FoodRepository foodRepository, ModelMapper modelMapper) {
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Food createFood(FoodRequestDTO foodRequestDTO, Category category, Restaurant restaurant) {
        Food food = modelMapper.map(foodRequestDTO, Food.class);
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);

        return foodRepository.save(food);
    }

    @Override
    public void deleteFood(Long id) throws FoodException {
        Food foodById = findFoodById(id);
        foodById.setRestaurant(null);
        foodRepository.save(foodById);
    }

    @Override
    public List<Food> getRestaurantFoods(Long restaurantId, boolean isVegeterian,
                                         boolean isNonveg, boolean isSeasonal, String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if (isVegeterian) {
            foods = filterByVegetarian(foods);
        }
        if (isNonveg) {
            foods = filterByNonveg(foods);
        }
        if (isSeasonal) {
            foods = filterBySeasonal(foods);
        }
        if (foodCategory != null && !foodCategory.isEmpty()) {
            foods = filterByCategory(foods, foodCategory);
        }

        return foods;
    }

    @Override
    public List<Food> searchFood(String query) {
        return foodRepository.searchFood(query);
    }

    @Override
    public Food findFoodById(Long id) throws FoodException {

        Optional<Food> food = foodRepository.findById(id);

        if (food.isEmpty())
            throw new FoodException("Food not found with id: " + id);

        return food.get();
    }

    @Override
    public Food updateAvailbilityStatus(Long id) throws FoodException {
        Food foodById = findFoodById(id);
        foodById.setAvailable(!foodById.isAvailable());
        return foodRepository.save(foodById);
    }

    private List<Food> filterByCategory(List<Food> foods, String category) {
        return foods.stream().filter(food -> Objects.nonNull(food.getFoodCategory()))
                .filter(food -> food.getFoodCategory().getName().equals(category))
                .collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods) {
        return foods.stream().filter(Food::isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonveg(List<Food> foods) {
        return foods.stream().filter(f -> !f.isVegetarian()).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods) {
        return foods.stream().filter(Food::isVegetarian).collect(Collectors.toList());
    }

}
