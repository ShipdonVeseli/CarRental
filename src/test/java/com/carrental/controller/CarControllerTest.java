package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import com.carrental.service.CarService;
import com.carrental.service.CurrencyService;
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarControllerTest {

    @Autowired
    CarController carController;

    @Test
    void createCarTest(){
        try {
            Car car=new Car();
            int availableSeats=19;
            double dayPrice=Math.PI;
            String transmission="testTransmission";

            car.setAvailableSeats(availableSeats);
            car.setTransmission(transmission);
            car.setDayPrice(dayPrice);

            ResponseEntity<Car> result= carController.createNewCar(car);

            System.out.println(result.toString());

            HttpStatus statusActuall=result.getStatusCode();
            HttpStatus statusExpected=HttpStatus.OK;

            Car actualCar=result.getBody();

            System.out.println(car);
            System.out.println(actualCar);

            assertEquals(statusExpected,statusActuall);
            assertEquals(car,actualCar);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getCars(){
        try{
            ResponseEntity<?> result= carController.getAvailableCars(CurrencyService.DATABASE_CURRENCY);
            System.out.println(result.getBody().toString());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getAllCars(){
        try{
            ResponseEntity<?> result= carController.getAllCars(CurrencyService.DATABASE_CURRENCY);
            System.out.println(result.getBody().toString());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getCar(){
        try{
            long index= 1;
            ResponseEntity<?> result= carController.getCar(index);
            System.out.println(result.getBody().toString());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

}