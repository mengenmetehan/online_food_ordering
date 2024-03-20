package com.mengen.request;

import com.mengen.model.Address;
import com.mengen.model.ContactInformation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data

public class RestaurantRequestDTO{
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private boolean open;
    private List<String> images;
}
