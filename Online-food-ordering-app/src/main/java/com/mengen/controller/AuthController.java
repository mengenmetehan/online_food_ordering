package com.mengen.controller;

import com.mengen.repository.UserRepository;
import com.mengen.security.JwtProvider;
import com.mengen.service.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private CustomerUserDetailsService customerUserDetailsService;

}
