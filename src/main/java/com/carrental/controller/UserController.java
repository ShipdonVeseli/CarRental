package com.carrental.controller;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.entity.Car;
import com.carrental.entity.User;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User newUser) {
        User userEntity = userService.createNewUser(newUser);
        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User userEntity = userService.getUser(user);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

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

    @PostMapping("/converter/{currency}")
    public ResponseEntity<?> changeCurrency(@PathVariable final String currency) {
        List<Double> allPrices = userService.getAllPrices();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
        ArrayOfdouble arrayOfdouble = new ArrayOfdouble();
        for (int i=0; i<allPrices.size(); i++) {
            arrayOfdouble.getDouble().add(allPrices.get(i));
        }
        return (ResponseEntity<?>) currencyClient.convertCurrencyListResponse(arrayOfdouble,"usd",currency).getConvertCurrencyListResult().getValue().getDouble();
    }
}
