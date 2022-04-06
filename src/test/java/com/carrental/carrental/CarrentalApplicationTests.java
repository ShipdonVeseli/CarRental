package com.carrental.carrental;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarrentalApplicationTests {

	@Autowired
	private CarRepository carRepository;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void createCars() {
		int numberOfCars = 10;
		for (int i=0; i<numberOfCars; i++) {
			Car car = new Car();
			car.setAvailableSeats(getRandomNumber(3,6));
			car.setDayPrice(getRandomNumber(1000,10000));
			car.setTransmission("Automatik");
			carRepository.save(car);
		}
	}

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

}
