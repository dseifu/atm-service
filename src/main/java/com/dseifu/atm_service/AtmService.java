package com.dseifu.atm_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public class AtmService {

    @Autowired
    private RestTemplate restTemplate;
    public HashMap<String, String> findByCardNumberAndCcv(String cardNumber, String ccv, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("cardNumber",cardNumber);
        formParameters.add("ccv",ccv);
        HashMap<String, String> response = restTemplate.postForObject("http://BANK-SERVICE/verify/card", formParameters, HashMap.class);
        if(response.get("status").equals("Verified"))
            httpServletRequest.getSession().setAttribute("isVerified","true");
        return response;
    }

    public String authenticateByPin(String pin, String cardNumber, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("pin",pin);
        formParameters.add("cardNumber",cardNumber);
        if(httpServletRequest.getSession().getAttribute("isVerified")!= null)
            formParameters.add("isVerified", httpServletRequest.getSession().getAttribute("isVerified").toString());
        String response = restTemplate.postForObject("http://BANK-SERVICE/Authenticate/pin", formParameters, String.class);
        if(response.equals("Authenticated"))
            httpServletRequest.getSession().setAttribute("isAuthenticated","true");
        return response;
    }

    public String authenticateByFingerPrint(String fingerPrint, String cardNumber, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("fingerPrint",fingerPrint);
        formParameters.add("cardNumber",cardNumber);
        if(httpServletRequest.getSession().getAttribute("isVerified")!= null)
            formParameters.add("isVerified", httpServletRequest.getSession().getAttribute("isVerified").toString());
        String response = restTemplate.postForObject("http://BANK-SERVICE/Authenticate/fingerPrint", formParameters, String.class);
        if(response.equals("Authenticated"))
            httpServletRequest.getSession().setAttribute("isAuthenticated","true");
        return response;
    }

    public String changeAuthenticationPreference(String cardNumber, String preference, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("cardNumber",cardNumber);
        formParameters.add("preference",preference);
        if(httpServletRequest.getSession().getAttribute("isAuthenticated")!= null)
            formParameters.add("isAuthenticated", httpServletRequest.getSession().getAttribute("isAuthenticated").toString());
        return restTemplate.postForObject("http://BANK-SERVICE/changeAuthenticationPreference",formParameters, String.class);
    }

    public String deposit(String cardNumber, String amount, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("cardNumber",cardNumber);
        formParameters.add("amount",amount);
        if(httpServletRequest.getSession().getAttribute("isAuthenticated")!= null)
            formParameters.add("isAuthenticated", httpServletRequest.getSession().getAttribute("isAuthenticated").toString());
        return restTemplate.postForObject("http://BANK-SERVICE/deposit",formParameters, String.class);
    }

    public String withdraw(String cardNumber, String amount, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("cardNumber",cardNumber);
        formParameters.add("amount",amount);
        if(httpServletRequest.getSession().getAttribute("isAuthenticated")!= null)
            formParameters.add("isAuthenticated", httpServletRequest.getSession().getAttribute("isAuthenticated").toString());
        return restTemplate.postForObject("http://BANK-SERVICE/withdraw",formParameters, String.class);
    }

    public String checkBalance(String cardNumber, HttpServletRequest httpServletRequest) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("cardNumber",cardNumber);
        if(httpServletRequest.getSession().getAttribute("isAuthenticated")!= null)
            formParameters.add("isAuthenticated", httpServletRequest.getSession().getAttribute("isAuthenticated").toString());
        return restTemplate.postForObject("http://BANK-SERVICE/checkBalance", formParameters, String.class);
    }

    public String done(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();
        return restTemplate.postForObject("http://BANK-SERVICE/done","", String.class);
    }
}
