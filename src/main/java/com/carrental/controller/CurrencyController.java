package com.carrental.controller;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.entity.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/converter")
public class CurrencyController {

    public static final String DATABASE_CURRENCY = "usd";
    private CarService carService;

    @Autowired
    public CurrencyController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/cars/{currency}")
    public ResponseEntity<List<Double>> changeCurrencyList(@PathVariable final String currency) {
        List<Double> allPrices = carService.getPricesOfAvailableCars();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
        ArrayOfdouble arrayOfdouble = new ArrayOfdouble();
        for (int car=0; car<allPrices.size(); car++) {
            arrayOfdouble.getDouble().add(allPrices.get(car));
        }
        List <Double> result = currencyClient.convertCurrencyListResponse(arrayOfdouble, DATABASE_CURRENCY,currency).getConvertCurrencyListResult().getValue().getDouble();
//        List<Car> cars = carService.getAvailableCars();
//        for(int i=0; i<cars.size(); i++) {
//            cars.get(i).setDayPrice(result.get(i));
//        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
