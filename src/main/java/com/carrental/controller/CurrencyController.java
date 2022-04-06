package com.carrental.controller;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private UserService userService;

    @Autowired
    public CurrencyController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/listconverter/{currency}")
    public ResponseEntity<?> changeCurrencyList(@PathVariable final String currency) {
        List<Double> allPrices = userService.getAllPrices();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
        ArrayOfdouble arrayOfdouble = new ArrayOfdouble();
        for (int i=0; i<allPrices.size(); i++) {
            arrayOfdouble.getDouble().add(allPrices.get(i));
        }
        return (ResponseEntity<?>) currencyClient.convertCurrencyListResponse(arrayOfdouble,"usd",currency).getConvertCurrencyListResult().getValue().getDouble();
    }
}
