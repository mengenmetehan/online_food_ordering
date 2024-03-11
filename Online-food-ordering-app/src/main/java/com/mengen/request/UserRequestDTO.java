package com.mengen.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mengen.enums.USER_ROLE;
import com.mengen.model.Address;
import com.mengen.model.Order;
import com.mengen.response.RestaurantDTO;
import com.mengen.response.RestaurantResponseDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
public class UserRequestDTO {

     String fullName;
     String email;
     String password;
     USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
     List<Order> orders = new ArrayList<>();
     List<RestaurantResponseDTO> favorites = new ArrayList<>();
     List<Address> addresses = new ArrayList<>();
}