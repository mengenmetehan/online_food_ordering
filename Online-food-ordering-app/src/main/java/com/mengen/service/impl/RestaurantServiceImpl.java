package com.mengen.service.impl;

import com.mengen.exceptions.RestaurantNotFoundException;
import com.mengen.exceptions.UserNotFoundException;
import com.mengen.model.Address;
import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.repository.AddressRepository;
import com.mengen.repository.RestaurantRepository;
import com.mengen.request.RestaurantRequestDTO;
import com.mengen.request.UserRequestDTO;
import com.mengen.response.RestaurantDTO;
import com.mengen.response.RestaurantResponseDTO;
import com.mengen.service.RestaurantService;
import com.mengen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {


    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserService userService;


    public RestaurantServiceImpl(ModelMapper modelMapper, RestaurantRepository restaurantRepository,
                                 AddressRepository addressRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    @Override
    public Restaurant createRestaurant(RestaurantRequestDTO restaurantDTO, UserRequestDTO userRequestDTO) throws UserNotFoundException {
        Address address = addressRepository.save(restaurantDTO.getAddress());
        User user;
        try {
            user = userService.findUserByEmail(userRequestDTO.getEmail());
        }
        catch (Exception e) {
            throw new UserNotFoundException("User not found with email: " + userRequestDTO.getEmail());
        }


        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        restaurant.setOwner(user);
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setAddress(address);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant createRestaurant(RestaurantRequestDTO restaurantDTO, User user) throws UserNotFoundException {
        return createRestaurant(restaurantDTO, modelMapper.map(user, UserRequestDTO.class));
    }

    @Override
    public RestaurantResponseDTO findByOwnerId(Long id) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findByOwnerId(id);

        if (Objects.isNull(restaurant))
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);


        return modelMapper.map(restaurant, RestaurantResponseDTO.class);
    }

    @Override
    public RestaurantResponseDTO findByOwnerEmail(String email) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findByOwnerEmail(email);

        if (Objects.isNull(restaurant))
            throw new RestaurantNotFoundException("Restaurant not found with email: " + email);


        return modelMapper.map(restaurant, RestaurantResponseDTO.class);
    }




    @Override
    public Restaurant updateRestaurant(Long id, RestaurantRequestDTO restaurantDTO) throws Exception {
        RestaurantResponseDTO restaurantById = findRestaurantById(id);

        if (Objects.isNull(restaurantById))
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);

        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) throws Exception {
        RestaurantResponseDTO restaurantById = findRestaurantById(id);

        if (Objects.isNull(restaurantById))
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);

        restaurantRepository.delete(modelMapper.map(restaurantById, Restaurant.class));
    }

    @Override
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantResponseDTO.class))
                .toList();
    }

    @Override
    public List<RestaurantResponseDTO> searchRestaurants(String query) {
        return restaurantRepository.findBySearchQuery(query).stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantResponseDTO.class))
                .toList();
    }

    @Override
    public RestaurantResponseDTO findRestaurantById(Long id) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if (restaurant.isEmpty())
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);

        return modelMapper.map(restaurant.get(), RestaurantResponseDTO.class);
    }

    @Override
    public RestaurantResponseDTO addToFavorites(Long id, UserRequestDTO userRequestDTO) throws Exception {
        RestaurantResponseDTO restaurantById = findRestaurantById(id);


        if (userRequestDTO.getFavorites().contains(restaurantById))
            userRequestDTO.getFavorites().remove(restaurantById);
        else
            userRequestDTO.getFavorites().add(restaurantById);

        userService.save(userRequestDTO);
        return restaurantById;
    }

    @Override
    public RestaurantResponseDTO updateRestaurantStatus(Long id) throws Exception {

        RestaurantResponseDTO restaurantById = findRestaurantById(id);
        restaurantById.setOpen(!restaurantById.isOpen());
        restaurantRepository.save(modelMapper.map( restaurantById, Restaurant.class));
        return restaurantById;
    }

    @Override
    public RestaurantResponseDTO removeFromFavorites(Long id, UserRequestDTO userRequestDTO) throws Exception {

        // TODO: Implement this
        return null;
    }
}
