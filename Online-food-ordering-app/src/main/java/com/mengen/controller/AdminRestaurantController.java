package com.mengen.controller;

import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.request.RestaurantRequestDTO;
import com.mengen.request.UserRequestDTO;
import com.mengen.response.RestaurantResponseDTO;
import com.mengen.service.RestaurantService;
import com.mengen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AdminRestaurantController(RestaurantService restaurantService, UserService userService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/user")
    public ResponseEntity<RestaurantResponseDTO> findRestaurantByUserId(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        //RestaurantResponseDTO restaurantResponseDTO = restaurantService.findByOwnerEmail(userRequestDTO.email());
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userByJwtToken.getId());

        return new ResponseEntity<>(modelMapper.map(restaurant, RestaurantResponseDTO.class), HttpStatus.CREATED);
    }


    @PostMapping()
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                                  //@RequestBody UserRequestDTO userRequestDTO,
                                                                  @RequestHeader("Authorization") String jwt) throws Exception
    {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.createRestaurant(restaurantRequestDTO,
                userByJwtToken);

        return new ResponseEntity<>(modelMapper.map(restaurant, RestaurantResponseDTO.class), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                                  @PathVariable Long id,
                                                                  @RequestHeader("Authorization") String jwt) throws Exception
    {
        Restaurant restaurant = restaurantService.updateRestaurant(id , restaurantRequestDTO);

        return new ResponseEntity<>(modelMapper.map(restaurant, RestaurantResponseDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String jwt) throws Exception
    {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurantStatus(//@RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                   @PathVariable Long id,
                                                   @RequestHeader("Authorization") String jwt) throws Exception
    {
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(modelMapper.map(restaurant, RestaurantResponseDTO.class), HttpStatus.OK);
    }
}
