package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private CarService carService;

    @Autowired
    public UserService(UserRepository userRepository, CarService carService) {
        this.userRepository = userRepository;
        this.carService = carService;
    }

    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    public User addCarToUser(Long userId, Long carId) {
        User user = getUser(userId).get();
        Car car = carService.getCar(carId).get();
        if(Objects.nonNull(car.getUser())) {
            return null;
        }
        user.addCar(car);
        car.setUser(user);
        return user;
    }

    @Transactional
    public User removeCarFromUser(Long userId, Long carId) {
        User user = getUser(userId).get();
        Car car = carService.getCar(carId).get();
        if(Objects.nonNull(car.getUser())) {
            return null;
        }
        user.removeCar(car);
        return user;
    }
}
