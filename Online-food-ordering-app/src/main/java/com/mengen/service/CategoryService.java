package com.mengen.service;

import com.mengen.exceptions.RestaurantNotFoundException;
import com.mengen.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name, Long userId) throws RestaurantNotFoundException;

    List<Category> findCategoryByRestaurantId(Long restaurantId);

    Category findCategoryById(Long id) throws Exception;

}
