package com.mengen.service.impl;

import com.mengen.exceptions.UserNotFoundException;
import com.mengen.model.Restaurant;
import com.mengen.model.User;
import com.mengen.repository.UserRepository;
import com.mengen.request.UserRequestDTO;
import com.mengen.response.RestaurantDTO;
import com.mengen.response.RestaurantResponseDTO;
import com.mengen.security.JwtProvider;
import com.mengen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.modelMapper = modelMapper;
    }
    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String emailFromJwtToken = jwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(emailFromJwtToken);
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);

        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found with email: " + email);

        return user;
    }

    public User save(UserRequestDTO userRequestDTO)  {
        User byEmail = userRepository.findByEmail(userRequestDTO.getEmail());
        if (Objects.nonNull(byEmail)) {
            byEmail.setFullName(userRequestDTO.getFullName());
            byEmail.setRole(userRequestDTO.getRole());
            byEmail.setAddresses(userRequestDTO.getAddresses());
            List<RestaurantResponseDTO> favorites = userRequestDTO.getFavorites();
            byEmail.setFavorites(favorites.stream()
                    .map(restaurantResponseDTO -> modelMapper.map(restaurantResponseDTO, RestaurantDTO.class))
                    .collect(Collectors.toList()));
            byEmail.setOrders(userRequestDTO.getOrders());
            return userRepository.save(byEmail);
        }

        return userRepository.save(modelMapper.map(userRequestDTO, User.class));
    }
}
