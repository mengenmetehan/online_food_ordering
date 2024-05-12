package com.mengen.controller;

import com.mengen.enums.USER_ROLE;
import com.mengen.model.Cart;
import com.mengen.model.User;
import com.mengen.repository.CartRepository;
import com.mengen.repository.UserRepository;
import com.mengen.request.LoginRequestDTO;
import com.mengen.response.AuthResponseDTO;
import com.mengen.security.JwtProvider;
import com.mengen.service.impl.CustomerUserDetailsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final CartRepository cartRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
                          CustomerUserDetailsService customerUserDetailsService, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customerUserDetailsService = customerUserDetailsService;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/register")
    //@CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<AuthResponseDTO> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if (Objects.nonNull(isEmailExist))
            return ResponseEntity.badRequest().body(new AuthResponseDTO(null, "Email already exist", null));

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setRole(user.getRole());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(newUser);


        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponseDTO authResponse = new AuthResponseDTO(jwt, "User created successfully", savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> loginHandler(@RequestBody LoginRequestDTO loginRequest) throws Exception {
        Authentication authentication = authenticateUser(loginRequest);
        String jwt = jwtProvider.generateToken(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        AuthResponseDTO authResponse = new AuthResponseDTO(jwt, "Login successful",
                USER_ROLE.valueOf( authorities.isEmpty() ? "ROLE_CUSTOMER" : authorities.iterator().next().getAuthority()));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticateUser(LoginRequestDTO loginRequest) {

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(loginRequest.getEmail());

        if (Objects.isNull(userDetails))
            throw new BadCredentialsException("Pls try again invalid email");

        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword()))
            throw new BadCredentialsException("Pls try again invalid password");

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
