package com.carrental.client;

import com.carrental.currency.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
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
       // JAXBElement<String> sourceCurrencyElement = factory.createConvertCurrencySourceCurrency(sourceCurrency);
      //  JAXBElement<String> destinationCurrencyElement = factory.createConvertCurrencyDestinationCurrency(destinationCurrency);
        getCurrencyRequest.setSourceCurrency((JAXBElement<String>)(factory.createConvertCurrencySourceCurrency(sourceCurrency)));
        getCurrencyRequest.setDestinationCurrency((JAXBElement<String>)factory.createConvertCurrencyDestinationCurrency(destinationCurrency));
        getCurrencyRequest.setValue(50.0);
        return (ConvertCurrencyResponse) getWebServiceTemplate().marshalSendAndReceive(getCurrencyRequest,new SoapActionCallback("http://tempuri.org/IService1/convertCurrency"));
    }
    public ConvertCurrencyListResponse convertCurrencyListResponse(ArrayOfdouble values, String sourceCurrency, String destinationCurrency)
    {
        ConvertCurrencyList getCurrencyRequestList = new ConvertCurrencyList();

        getCurrencyRequestList.setValues((JAXBElement<ArrayOfdouble>) factory.createConvertCurrencyListValues(values));
        getCurrencyRequestList.setSourceCurrency((JAXBElement<String>) factory.createConvertCurrencySourceCurrency(sourceCurrency));
        getCurrencyRequestList.setDestinationCurrency((JAXBElement<String>) factory.createConvertCurrencyDestinationCurrency(destinationCurrency));
        return (ConvertCurrencyListResponse) getWebServiceTemplate().marshalSendAndReceive(getCurrencyRequestList,new SoapActionCallback("http://tempuri.org/IService1/convertCurrencyList")) ;
        //hello
       // return (ConvertCurrencyResponse) getWebServiceTemplate().marshalSendAndReceive(getCurrencyRequest,new SoapActionCallback("http://tempuri.org/IService1/convertCurrency"));
    }


}
