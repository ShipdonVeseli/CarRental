package com.carrental.repository;

import com.carrental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("FROM Car WHERE user.id IS NULL")
    List<Car> getAvailableCars();

    @Query("SELECT dayPrice FROM Car")
    List<Double> getAllPrices();
}
