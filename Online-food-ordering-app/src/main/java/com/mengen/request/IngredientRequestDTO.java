package com.mengen.request;

import lombok.Data;

@Data
public class IngredientRequestDTO {

    private String name;
    private Long categoryId;
    private Long restaurantId;
}
