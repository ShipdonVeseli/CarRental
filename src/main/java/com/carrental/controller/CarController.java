package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> createNewCar(@RequestBody Car newCar) {
        Car carEntity = carService.createNewCar(newCar);
        return new ResponseEntity<>(carEntity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable("id") long id) {
        Optional<Car> car = carService.getCar(id);
        if(car.isPresent()) {
            return new ResponseEntity<>(car.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
