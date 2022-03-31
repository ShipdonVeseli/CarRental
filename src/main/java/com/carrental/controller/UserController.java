package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.service.CarService;
import com.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private CarService carService;

    @Autowired
    public UserController(UserService userService, CarService carService) {
        this.userService = userService;
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
        User userEntity = userService.createNewUser(newUser);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<User> getUser(@RequestBody User user) {
        User userEntity = userService.getUser(user);
        return ResponseEntity.ok(userEntity);
    }

    @PostMapping("/{userId}/cars/{carId}")
    public ResponseEntity<Car> rentCar(@PathVariable String userId, @PathVariable String carId) {
        Optional<User> user = userService.getUser(Long.parseLong(userId));
        if (user.isPresent()) {
            User userEntity = user.get();
            Optional<Car> car = carService.getCar(Long.parseLong(carId));
            if (car.isPresent()) {
                Car carEntity = car.get();

                List<Car> cars = userEntity.getCars();
                cars.add(carEntity);
                userEntity.setCars(cars);
                userService.updateUser(userEntity);
                return ResponseEntity.ok(carEntity);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


