package com.carrental.controller;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.entity.Car;
import com.carrental.service.CarService;
import com.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/converter")
public class CurrencyController {

    private static final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
    private static final CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
    public static final String DATABASE_CURRENCY = "usd";
    private CarService carService;
    private UserService userService;

    @Autowired
    public CurrencyController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @PostMapping("/cars/availableCars/{currency}")
    public ResponseEntity<List<Car>> convertAvailableCars(@PathVariable final String currency) {
        List<Car> carsToConvert = carService.getAvailableCars();
        List<Car> convertedCars = getConvertedList(carsToConvert, currency);
        return new ResponseEntity<>(convertedCars,HttpStatus.OK);
    }

    @PostMapping("/cars/{currency}")
    public ResponseEntity<List<Car>> convertAllCars(@PathVariable final String currency) {
        List<Car> carsToConvert = carService.getAllCars();
        List<Car> convertedCars = getConvertedList(carsToConvert, currency);
        return new ResponseEntity<>(convertedCars, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/cars/{currency}")
    public ResponseEntity<List<Car>> convertCarsFromUser(@PathVariable final Long userId, @PathVariable final String currency) {
        List<Car> carsToConvert = userService.getUser(userId).getCars();
        List<Car> convertedCars = getConvertedList(carsToConvert, currency);
        return new ResponseEntity<>(convertedCars, HttpStatus.OK);
    }

    public List<Car> getConvertedList(List<Car> cars, String currency) {
        ArrayOfdouble arrayOfdouble = new ArrayOfdouble();
        for(int i=0; i<cars.size(); i++) {
            arrayOfdouble.getDouble().add(cars.get(i).getDayPrice());
        }
        List <Double> result = currencyClient.convertCurrencyListResponse(arrayOfdouble, DATABASE_CURRENCY,currency).getConvertCurrencyListResult().getValue().getDouble();
        for(int i=0; i<cars.size(); i++) {
            cars.get(i).setDayPrice(result.get(i));
        }
        return cars;
    }

}
