package com.mengen.service.impl;

import com.mengen.exceptions.RestaurantNotFoundException;
import com.mengen.model.Category;
import com.mengen.model.Restaurant;
import com.mengen.repository.CategoryRepository;
import com.mengen.service.CategoryService;
import com.mengen.service.RestaurantService;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final RestaurantService restaurantService;

    private final ModelMapper modelMapper;

    private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }


    @Override
    public Category createCategory(String name, Long userId) {
        Restaurant restaurantById = null;
        try {
            restaurantById = restaurantService.getRestaurantByUserId(userId);
        }
        catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found");
        }

        if (Objects.isNull(restaurantById))
            return null;

        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurantById);
        return categoryRepository.save(category);
        }

    @Override
    public List<Category> findCategoryByRestaurantId(Long restaurantId) {
        Restaurant restaurant = null;

        try {
            restaurant = restaurantService.findRestaurantById(restaurantId);
        }
        catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found");
        }
        Objects.requireNonNullElse(restaurant, null);

        return categoryRepository.findByRestaurantId(restaurant.getId());
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        return categoryRepository.findById(id).orElseThrow(() -> new Exception("Category not found"));
    }
}
