package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

}
