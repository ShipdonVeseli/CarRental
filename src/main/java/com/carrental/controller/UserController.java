package com.carrental.controller;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.entity.exception.JWTParseException;
import com.carrental.service.JWTService;
import com.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    final JWTService jwtService;
    @Autowired
    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User newUser) {
        User userEntity = userService.createNewUser(newUser);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User userEntity = userService.getUser(user);
        Long id = userService.getUserIdIfPasswordMatches(userEntity);
        return ResponseEntity.ok(jwtService.buildJWT(id));
    }

   /* @GetMapping("/users/{userId}/cars")
    public ResponseEntity<List<Car>> getCars(@PathVariable final Long userId) {
        List<Car> carsFromUser = userService.getCars(userId);
        return new ResponseEntity<>(carsFromUser, HttpStatus.OK);
    }
    */
   @GetMapping("/users/cars")
   public ResponseEntity<?> getCars(@RequestHeader("Authorization") String authHeader) throws JWTParseException {
       Long id = jwtService.parseJWT(authHeader);
       List<Car> carsFromUser = userService.getCars(id);
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

}
