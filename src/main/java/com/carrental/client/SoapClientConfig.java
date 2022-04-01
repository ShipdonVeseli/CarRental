package com.carrental.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.carrental.currency");
        return jaxb2Marshaller;
    }

    @Bean
    public CurrencyClient articleClient(Jaxb2Marshaller jaxb2Marshaller) {
        CurrencyClient currencyClient = new CurrencyClient();
        currencyClient.setDefaultUri("http://localhost:56969/CurrencyService.svc");
        currencyClient.setMarshaller(jaxb2Marshaller);
        currencyClient.setUnmarshaller(jaxb2Marshaller);
        return currencyClient;
    }
}
