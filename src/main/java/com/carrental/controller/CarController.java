package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.entity.exception.CarDoesNotExistsException;
import com.carrental.service.CarService;
import com.carrental.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;
    private CurrencyService currencyService;

    @Autowired
    public CarController(CarService carService, CurrencyService currencyService) {
        this.carService = carService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<Car> createNewCar(@RequestBody Car newCar) {
        Car carEntity = carService.createNewCar(newCar);
        return new ResponseEntity<>(carEntity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllCars(@RequestParam(name = "currency") String currency) {
        if(currencyService.checkIfValidCurrency(currency)) {
            return new ResponseEntity<>("Invalid Currency", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        List<Car> cars = carService.getAllCars();
        if(!currency.equals(CurrencyService.DATABASE_CURRENCY)) {
            cars = currencyService.convertListOfCars(cars, currency);
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCar(@PathVariable("id") Long id) {
        try {
            Car car = carService.getCar(id);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (CarDoesNotExistsException e) {
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/availableCars")
    public ResponseEntity<?> getAvailableCars(@RequestParam(name = "currency") String currency) {
        if(currencyService.checkIfValidCurrency(currency)) {
            return new ResponseEntity<>("Invalid Currency", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        List<Car> availableCars = carService.getAvailableCars();
        if(!currency.equals(CurrencyService.DATABASE_CURRENCY)) {
            availableCars = currencyService.convertListOfCars(availableCars, currency);
        }
        return new ResponseEntity<>(availableCars, HttpStatus.OK);
    }

}
