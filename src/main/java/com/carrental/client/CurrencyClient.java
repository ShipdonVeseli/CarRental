package com.carrental.client;

import com.carrental.currency.ObjectFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import com.carrental.currency.ConvertCurrency;
import com.carrental.currency.ConvertCurrencyResponse;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;


public class CurrencyClient extends WebServiceGatewaySupport {

    private ObjectFactory factory;

    public CurrencyClient()
    {
        factory = new ObjectFactory();
    }
    public ConvertCurrencyResponse getCurrencyResponse(double value, String sourceCurrency,String destinationCurrency)
    {
        ConvertCurrency getCurrencyRequest = new ConvertCurrency();
        JAXBElement<String> sourceCurrency2 = factory.createConvertCurrencySourceCurrency(sourceCurrency);
        JAXBElement<String> destinationCurrency2 = factory.createConvertCurrencyDestinationCurrency(destinationCurrency);
        getCurrencyRequest.setSourceCurrency(sourceCurrency2);
        getCurrencyRequest.setDestinationCurrency(destinationCurrency2);
        getCurrencyRequest.setValue(50.0);
        ObjectFactory objectFactory = new ObjectFactory();

        return (ConvertCurrencyResponse) getWebServiceTemplate().marshalSendAndReceive(getCurrencyRequest,new SoapActionCallback("http://tempuri.org/IService1/convertCurrency"));
    }

}
