package com.carrental;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class CarrentalApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(CarrentalApplication.class, args);

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
		CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
		System.out.println(currencyClient.getCurrencyResponse(50.0,"usd","try").getConvertCurrencyResult().doubleValue());

	}

}
