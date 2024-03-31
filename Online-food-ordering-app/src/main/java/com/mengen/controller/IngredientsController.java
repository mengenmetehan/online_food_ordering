package com.mengen.controller;

import com.mengen.model.IngredientCategory;
import com.mengen.model.IngredientsItem;
import com.mengen.request.IngredientCategoryRequestDTO;
import com.mengen.request.IngredientRequestDTO;
import com.mengen.service.IngredientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientsController {

    private final IngredientsService ingredientsService;

    public IngredientsController(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody IngredientCategoryRequestDTO request) throws Exception {

        return ResponseEntity.ok(ingredientsService.createIngredientCategory(request.getName(), request.getRestaurantId()));
    }

    @PostMapping()
    public ResponseEntity<IngredientsItem> createIngredientCategory(@RequestBody IngredientRequestDTO request) throws Exception {

        return ResponseEntity.ok(ingredientsService.createIngredientItem(request.getRestaurantId(),request.getName(), request.getCategoryId()));
    }

    @PutMapping("/{id}/stoke")
    public ResponseEntity<IngredientsItem> updateIngredientStock(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok(ingredientsService.updateStock(id));
    }
}
