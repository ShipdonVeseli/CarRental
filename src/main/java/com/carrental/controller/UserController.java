package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.model.JwtRequest;
import com.carrental.model.JwtResponse;
import com.carrental.service.UserService;
import com.carrental.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    private JwtUtility jwtUtility;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserService userService, JwtUtility jwtUtility, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User userEntity = userService.createNewUser(newUser);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody User user) {
//        User userEntity = userService.getUser(user);
//        return new ResponseEntity<>(userEntity, HttpStatus.OK);
//    }

    @GetMapping("/users/{userId}/cars")
    public ResponseEntity<List<Car>> getCars(@PathVariable final Long userId) {
        List<Car> carsFromUser = userService.getCars(userId);
        return new ResponseEntity<>(carsFromUser, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/cars/{carId}")
    public ResponseEntity<User> addCarToUser(@PathVariable final Long userId, @PathVariable final Long carId) {
        User user = userService.addCarToUser(userId, carId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/cars/{carId}")
    public ResponseEntity<User> removeCarFromUser(@PathVariable final Long userId, @PathVariable final Long carId) {
        User user = userService.removeCarFromUser(userId, carId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtUtility.generateToken(userDetails);
        return  new JwtResponse(token);
    }

}
