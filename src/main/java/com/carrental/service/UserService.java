package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.entity.exception.CarIsAlreadyAssignedException;
import com.carrental.entity.exception.UserHasNotThisCarException;
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

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    public boolean checkIfUserHasCar(Long userId, Long carId) {
        User user = getUser(userId);
        Car car = carService.getCar(carId);
        for (int i=0; i<user.getCars().size(); i++) {
            if (user.getCars().get(i).equals(car)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public User addCarToUser(Long userId, Long carId) {
        User user = getUser(userId);
        Car car = carService.getCar(carId);
        if(Objects.nonNull(car.getUser())) {
            throw new CarIsAlreadyAssignedException(carId);
        }
        user.addCar(car);
        car.setUser(user);
        return user;
    }

    @Transactional
    public User removeCarFromUser(Long userId, Long carId) {
        User user = getUser(userId);
        Car car = carService.getCar(carId);
        if (checkIfUserHasCar(userId, carId)) {
            user.removeCar(car);
            return user;
        }
        throw new UserHasNotThisCarException(carId);
    }
}
