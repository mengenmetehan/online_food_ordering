package com.mengen.request;

import lombok.Data;

@Data
public class UpdateCartRequestDTO {
    private Long cartItemId;
    private int quantity;
}
