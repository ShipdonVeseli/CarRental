package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.exception.CarDoesNotExistsException;
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
       /* if(car.isEmpty()) {
            throw new CarDoesNotExistsException(id);
        }*/
        return car.get();
    }

    public List<Car> getAvailableCars() {
        return carRepository.getAvailableCars();
    }

    public List<Double> getPricesOfAvailableCars() {
        return carRepository.getPricesOfAvailableCars();
    }

    public List<Double> getPricesOfAllCars() {
        return carRepository.getPricesOfAllCars();
    }
}
