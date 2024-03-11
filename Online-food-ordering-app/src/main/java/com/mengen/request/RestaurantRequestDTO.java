package com.mengen.request;

import com.mengen.model.Address;
import com.mengen.model.ContactInformation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class RestaurantRequestDTO{
    Long id;
    String name;
    String description;
    String cuisineType;
    Address address;
    ContactInformation contactInformation;
    String openingHours;
    boolean open;
    List<String> images;
}
