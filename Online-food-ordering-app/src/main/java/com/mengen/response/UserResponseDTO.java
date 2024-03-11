package com.mengen.response;

import com.mengen.model.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class UserResponseDTO {
    String fullName;
    String email;
    List<RestaurantResponseDTO> favorites;
    List<Address> addresses;
    String role;
}
