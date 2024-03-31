package com.mengen.request;

import lombok.Data;

import java.util.List;

@Data
public class AddCartItemRequestDTO {

    private Long foodId;
    private int quantity;
    private List<String> ingredients;
}
