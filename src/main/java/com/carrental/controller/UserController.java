package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.entity.exception.CarIsAlreadyAssignedException;
import com.carrental.entity.exception.UserHasNotThisCarException;
import com.carrental.entity.exception.UsernameAlreadyExistsException;
import com.carrental.model.JwtRequest;
import com.carrental.model.JwtResponse;
import com.carrental.service.CurrencyService;
import com.carrental.service.UserService;
import com.carrental.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    private CurrencyService currencyService;
    private JwtUtility jwtUtility;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, CurrencyService currencyService, JwtUtility jwtUtility, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.currencyService = currencyService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        try {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            User userEntity = userService.createNewUser(newUser);
            return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
        } catch (UsernameAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid Credentials", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtUtility.generateToken(userDetails);
        User user = userService.getUserByUsername(userDetails.getUsername());
        return new ResponseEntity<>(new JwtResponse(token, user.getId()), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/cars")
    public ResponseEntity<?> getCars(@PathVariable final Long userId, @RequestParam(name = "currency") String currency) {
        if(currencyService.checkIfValidCurrency(currency)) {
            return new ResponseEntity<>("Invalid Currency", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        List<Car> carsFromUser = userService.getCars(userId);
        if(!currency.equals(CurrencyService.DATABASE_CURRENCY)) {
            carsFromUser = currencyService.convertListOfCars(carsFromUser, currency);
        }
        return new ResponseEntity<>(carsFromUser, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/cars/{carId}")
    public ResponseEntity<?> addCarToUser(@PathVariable final Long userId, @PathVariable final Long carId) {
        try {
            User user = userService.addCarToUser(userId, carId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (CarIsAlreadyAssignedException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/users/{userId}/cars/{carId}")
    public ResponseEntity<?> removeCarFromUser(@PathVariable final Long userId, @PathVariable final Long carId) {
        try {
            User user = userService.removeCarFromUser(userId, carId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserHasNotThisCarException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
        }
    }

}
