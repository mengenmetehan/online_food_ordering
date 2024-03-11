package com.mengen.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mengen.model.Address;
import com.mengen.model.ContactInformation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Setter
@Getter
public class RestaurantResponseDTO{

    @JsonProperty
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

