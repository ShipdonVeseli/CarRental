package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createNewCar(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
    public Car getCar(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if(car.isPresent()) {
            return car.get();
        } else {
            return null;
        }
    }
}
