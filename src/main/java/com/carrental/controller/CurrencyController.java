package com.carrental.controller;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.currency.ConvertCurrencyResponse;
import com.carrental.model.CurrencyRequest;
import com.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    public static final String DATABASE_CURRENCY = "usd";
    private UserService userService;

    @Autowired
    public CurrencyController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/listconverter/{currency}")
    public ResponseEntity<List<Double>> changeCurrencyList(@PathVariable final String currency) {
        List<Double> allPrices = userService.getAllPrices();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
        ArrayOfdouble arrayOfdouble = new ArrayOfdouble();
        for (int car=0; car<allPrices.size(); car++) {
            arrayOfdouble.getDouble().add(allPrices.get(car));
        }
        List <Double> result = currencyClient.convertCurrencyListResponse(arrayOfdouble, DATABASE_CURRENCY,currency).getConvertCurrencyListResult().getValue().getDouble();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/convertToDollar")
    public ResponseEntity<Double> changeCurrency(@RequestBody CurrencyRequest currencyRequest) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);

        Double result = currencyClient.getCurrencyResponse(currencyRequest.getValue(), currencyRequest.getSourceCurrency(), DATABASE_CURRENCY).getConvertCurrencyResult();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
