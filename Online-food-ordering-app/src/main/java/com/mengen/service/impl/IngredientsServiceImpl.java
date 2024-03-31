package com.mengen.service.impl;

import com.mengen.model.IngredientCategory;
import com.mengen.model.IngredientsItem;
import com.mengen.model.Restaurant;
import com.mengen.repository.IngredientItemRepository;
import com.mengen.repository.IngredientCategoryRepository;
import com.mengen.service.IngredientsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientsServiceImpl implements IngredientsService {

    private final IngredientItemRepository ingredientItemRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private RestaurantServiceImpl restaurantService;
    private ModelMapper modelMapper;

    public IngredientsServiceImpl(IngredientItemRepository ingredientItemRepository, IngredientCategoryRepository ingredientCategoryRepository, RestaurantServiceImpl restaurantService, ModelMapper modelMapper) {
        this.ingredientItemRepository = ingredientItemRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }

    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurantById = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory category = new IngredientCategory();
        category.setName(name);
        category.setRestaurant(restaurantById);
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> category = ingredientCategoryRepository.findById(id);
        return category.orElseThrow(() -> new Exception("Category not found"));
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception {
        restaurantService.findRestaurantById(restaurantId);
        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception {
        Restaurant restaurantById = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = findIngredientCategoryById(categoryId);
        IngredientsItem ingredientsItem = new IngredientsItem();
        ingredientsItem.setName(ingredientName);
        ingredientsItem.setRestaurant(restaurantById);
        ingredientsItem.setCategory(category);

        IngredientsItem ingredients = ingredientItemRepository.save(ingredientsItem);
        category.getIngredients().add(ingredients);

        return ingredients;
    }

    @Override
    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem updateStock(Long id) {
        Optional<IngredientsItem> byId = ingredientItemRepository.findById(id);
        IngredientsItem ingredientsItem = byId.orElseThrow(() -> new RuntimeException("Ingredient not found"));
        ingredientsItem.setInStoke(!ingredientsItem.isInStoke());

        return ingredientItemRepository.save(ingredientsItem);
    }
}
