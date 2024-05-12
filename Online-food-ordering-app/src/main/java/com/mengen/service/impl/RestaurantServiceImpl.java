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
import org.springframework.cache.annotation.CachePut;
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
    public Restaurant createRestaurant(RestaurantRequestDTO restaurantDTO, User user) {
        Address address = addressRepository.save(restaurantDTO.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setCuisineType(restaurantDTO.getCuisineType());
        restaurant.setContactInformation(restaurantDTO.getContactInformation());
        restaurant.setImages(restaurantDTO.getImages());
        restaurant.setOpeningHours(restaurantDTO.getOpeningHours());
        restaurant.setOwner(user);
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setAddress(address);

        return restaurantRepository.save(restaurant);
    }


    @Override
    public Restaurant getRestaurantByUserId(Long id) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findByOwnerId(id);

        if (Objects.isNull(restaurant))
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);


        return restaurant;
    }

    @Override
    public Restaurant getRestaurantByEmail(String email) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findByOwnerEmail(email);

        if (Objects.isNull(restaurant))
            throw new RestaurantNotFoundException("Restaurant not found with email: " + email);


        return restaurant;
    }




    @Override
    //@CachePut(cacheNames = "restaurants", key = "'updateRestaurant' + #id", unless = "#result == null")
    public Restaurant updateRestaurant(Long id, RestaurantRequestDTO restaurantDTO) throws Exception {
        Restaurant restaurant = findRestaurantById(id);

        if (Objects.isNull(restaurant))
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);

        if (restaurant.getCuisineType() != null) {
            restaurant.setCuisineType(restaurantDTO.getCuisineType());
        }
        if (restaurant.getDescription() != null) {
            restaurant.setDescription(restaurantDTO.getDescription());
        }
        if (restaurant.getName() != null) {
            restaurant.setName(restaurantDTO.getName());
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);

        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurants(String query) {
        return restaurantRepository.findBySearchQuery(query);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if (restaurant.isEmpty())
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);

        return restaurant.get();
    }

    @Override
    public RestaurantDTO addToFavorites(Long id, User user) throws Exception {
        Restaurant restaurantById = findRestaurantById(id);

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setDescription(restaurantById.getDescription());
        restaurantDTO.setTitle(restaurantById.getName());
        restaurantDTO.setImages(restaurantById.getImages());
        restaurantDTO.setId(restaurantById.getId());

        boolean isFavorite = false;

        for (RestaurantDTO favorite : user.getFavorites()) {
            if (favorite.getId().equals(restaurantById.getId())) {
                isFavorite = true;
                break;
            }
        }
        if (!isFavorite) {
            user.getFavorites().add(restaurantDTO);
        } else {
            user.getFavorites().removeIf(favorite -> favorite.getId().equals(restaurantById.getId()));
        }


        userService.save(user);
        return restaurantDTO;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {

        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant removeFromFavorites(Long id, UserRequestDTO userRequestDTO) throws Exception {
        throw new UnsupportedOperationException("TODO");
    }
}
