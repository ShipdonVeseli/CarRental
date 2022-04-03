package com.carrental.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.*;

@Configuration
public class SoapClientConfig {

    public static String baseNameSpace;
    public static String baseURI;
    private final String path = "src/main/resources/wsdl/currencyConverter.wsdl";
    public SoapClientConfig() throws Exception {
        try
        {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String rootNameSpace = null;

            String wsdlLine;
            while((wsdlLine = bufferedReader.readLine()) != null)
            {
                if(wsdlLine.contains("targetNamespace") && rootNameSpace == null)
                {
                    int targetIndex = wsdlLine.indexOf("targetNamespace");
                    int lastIndex = wsdlLine.lastIndexOf("\"");
                    String result = wsdlLine.substring(targetIndex,wsdlLine.length());
                    rootNameSpace = getWSDLSubstring(result);
                }
                if(wsdlLine.contains("portType") && baseNameSpace == null && rootNameSpace != null)
                {
                    baseNameSpace= rootNameSpace+"/"+ getWSDLSubstring(wsdlLine) +"/";
                }
                else if(wsdlLine.contains("location") && baseURI == null)
                {
                    baseURI = getWSDLSubstring(wsdlLine);
                }
            }
            System.out.println("This is baseNameSpace: "+rootNameSpace);
            System.out.println("This is baseURI: "+ baseURI);
        }
        catch (Exception e)
        {
            throw new Exception("Exception while finding out BaseNameSpace");
        }


    }

    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.carrental.currency");
        return jaxb2Marshaller;
    }

    @Bean
    public CurrencyClient articleClient(Jaxb2Marshaller jaxb2Marshaller) {
        CurrencyClient currencyClient = new CurrencyClient();
        currencyClient.setDefaultUri(baseURI);
        currencyClient.setMarshaller(jaxb2Marshaller);
        currencyClient.setUnmarshaller(jaxb2Marshaller);
        return currencyClient;
    }

    public String getWSDLSubstring(String line)
    {
       return line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
    }
}
