package com.mengen.service;

import com.mengen.exceptions.UserNotFoundException;
import com.mengen.model.User;
import com.mengen.request.UserRequestDTO;

public interface UserService {
    
    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws UserNotFoundException;

    User save(UserRequestDTO user);
}
