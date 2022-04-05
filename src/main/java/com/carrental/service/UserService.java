package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.entity.exception.CarIsAlreadyAssignedException;
import com.carrental.entity.exception.UserHasNotThisCarException;
import com.carrental.entity.exception.UsernameAlreadyExistsException;
import com.carrental.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
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
        if(!checkIfUsernameExists(user.getUsername())) {
            return userRepository.save(user);
        } else {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }

    public boolean checkIfUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User getUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new IllegalArgumentException();
        }
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

    public List<Car> getCars(Long userId) {
        User user = getUser(userId);
        return user.getCars();
    }

    public Long getUserIdIfPasswordMatches(User user) {
        User user2 = userRepository.findByUsername(user.getUsername()).get();
        if(!digest(user.getPassword()).equals(user2.getPassword())){
            System.out.println("error");
        }
        return user.getId();
    }

    @SneakyThrows
    private String digest(String preDigest){
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return new String(md.digest(preDigest.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
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

    public List<Double> getAllPrices() {
        return carService.getAllPrices();
    }
}
