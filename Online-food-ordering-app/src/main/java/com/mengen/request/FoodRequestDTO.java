package com.mengen.request;

import com.mengen.model.Category;
import com.mengen.model.IngredientsItem;
import lombok.Data;

import java.util.List;

@Data
public class FoodRequestDTO {
    private String name;
    private String description;
    private Long price;

    private Category category;
    private List<String> images;

    private Long restaurantId;
    private boolean vegeterian;
    private boolean seasonal;
    private List<IngredientsItem> ingredients;
}
