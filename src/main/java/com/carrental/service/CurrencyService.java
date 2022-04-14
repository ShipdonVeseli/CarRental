package com.carrental.service;

import com.carrental.client.CurrencyClient;
import com.carrental.client.SoapClientConfig;
import com.carrental.currency.ArrayOfdouble;
import com.carrental.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService {

    private static final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SoapClientConfig.class);
    private static final CurrencyClient currencyClient = annotationConfigApplicationContext.getBean(CurrencyClient.class);
    public static final String DATABASE_CURRENCY = "USD";
    private CarService carService;
    private UserService userService;
    private ArrayList<String> currencies = new ArrayList<>();

    @Autowired
    public CurrencyService(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
        getCurrencies();
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

    private void getCurrencies() {
        try {
            URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(url.openStream());
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals("Cube") && node.hasAttribute("currency")) {
                        currencies.add(node.getAttribute("currency"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currencies.forEach(System.out::println);
    }

    private void addCurrencies() {
        currencies.add("USD");
        currencies.add("EUR");
        currencies.add("JPY");
        currencies.add("BGN");
        currencies.add("CZK");
        currencies.add("DKK");
        currencies.add("GBP");
        currencies.add("HUF");
        currencies.add("PLN");
        currencies.add("RON");
        currencies.add("SEK");
        currencies.add("CHF");
        currencies.add("ISK");
        currencies.add("NOK");
        currencies.add("HRK");
        currencies.add("TRY");
        currencies.add("AUD");
        currencies.add("BRL");
        currencies.add("CAD");
        currencies.add("CNY");
        currencies.add("HKD");
        currencies.add("IDR");
        currencies.add("ILS");
        currencies.add("INR");
        currencies.add("KRW");
        currencies.add("MXN");
        currencies.add("MYR");
        currencies.add("NZD");
        currencies.add("PHP");
        currencies.add("SGD");
        currencies.add("THB");
        currencies.add("ZAR");
    }

    public boolean checkIfValidCurrency(String currency) {
        return currencies.contains(currency);
    }
}
