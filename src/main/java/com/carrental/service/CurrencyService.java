package com.carrental.service;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private static final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
    private static final CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
    public static final String DATABASE_CURRENCY = "USD";
    private CarService carService;
    private UserService userService;

    @Autowired
    public CurrencyService(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    public List<Car> convertListOfCars(List<Car> carsToConvert, String currency) {
        return getConvertedList(carsToConvert, currency);
    }

    public List<Car> getConvertedList(List<Car> cars, String currency) {
        ArrayOfdouble priceList = new ArrayOfdouble();
        for(int i=0; i<cars.size(); i++) {
            priceList.getDouble().add(cars.get(i).getDayPrice());
        }
        List <Double> result = currencyClient.convertCurrencyListResponse(priceList, DATABASE_CURRENCY,currency).getConvertCurrencyListResult().getValue().getDouble();
        for(int i=0; i<cars.size(); i++) {
            cars.get(i).setDayPrice(result.get(i));
        }
        return cars;
    }
}
