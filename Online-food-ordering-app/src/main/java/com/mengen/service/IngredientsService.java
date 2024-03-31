package com.mengen.service;

import com.mengen.model.IngredientCategory;
import com.mengen.model.IngredientsItem;

import java.util.List;

public interface IngredientsService {

    IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;

    IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception;

    IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception;

    List<IngredientsItem> findRestaurantsIngredients(Long restaurantId);

    IngredientsItem updateStock(Long id);
}
