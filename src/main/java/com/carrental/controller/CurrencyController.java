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

import java.util.ArrayList;
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
        List<Double> allPrices = carService.getPricesOfAvailableCars();
        List<Double> result = getConvertedList(allPrices, currency);
        List<Car> cars = carService.getAvailableCars();
        for(int i=0; i<cars.size(); i++) {
            cars.get(i).setDayPrice(result.get(i));
        }
        return new ResponseEntity<>(cars,HttpStatus.OK);
    }

    @PostMapping("/cars/{currency}")
    public ResponseEntity<List<Car>> convertAllCars(@PathVariable final String currency) {
        List<Double> prices = carService.getPricesOfAllCars();
        List<Double> result = getConvertedList(prices, currency);
        List<Car> cars = carService.getAllCars();
        for(int i=0; i<cars.size(); i++) {
            cars.get(i).setDayPrice(result.get(i));
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/cars/{currency}")
    public ResponseEntity<List<Car>> convertCarsFromUser(@PathVariable final Long userId, @PathVariable final String currency) {
        List<Car> cars = userService.getUser(userId).getCars();
        List<Double> prices = new ArrayList<>();
        for(int i=0; i<cars.size(); i++) {
            prices.add(cars.get(i).getDayPrice());
        }
        List<Double> result = getConvertedList(prices, currency);
        for(int i=0; i<cars.size(); i++) {
            cars.get(i).setDayPrice(result.get(i));
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    public List<Double> getConvertedList(List<Double> list, String currency) {
        ArrayOfdouble arrayOfdouble = new ArrayOfdouble();
        for(int i=0; i<list.size(); i++) {
            arrayOfdouble.getDouble().add(list.get(i));
        }
        List <Double> result = currencyClient.convertCurrencyListResponse(arrayOfdouble, DATABASE_CURRENCY,currency).getConvertCurrencyListResult().getValue().getDouble();
        return result;
    }

}
