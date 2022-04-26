package com.dseifu.atm_service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class AtmController {

    @Autowired
    private AtmService atmService;

    @PostMapping("/verify/card")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForFindByCardNumberAndCcv")
    public HashMap<String, String> findByCardNumberAndCcv(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest)
    {
        return  atmService.findByCardNumberAndCcv(formParameters.getFirst("cardNumber"),formParameters.getFirst("ccv"), httpServletRequest);
    }

    @PostMapping("/Authenticate/pin")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String authenticateByPin(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest){
        return  atmService.authenticateByPin(formParameters.getFirst("pin"), formParameters.getFirst("cardNumber"), httpServletRequest);
    }

    @PostMapping("/Authenticate/fingerPrint")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String authenticateByFingerPrint(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest){
        return  atmService.authenticateByFingerPrint(formParameters.getFirst("fingerPrint"), formParameters.getFirst("cardNumber"), httpServletRequest);
    }

    @PostMapping("/changeAuthenticationPreference")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String changeAuthenticationPreference(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest){
        return  atmService.changeAuthenticationPreference(formParameters.getFirst("cardNumber"), formParameters.getFirst("preference"), httpServletRequest);
    }

    @PostMapping("/deposit")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String deposit(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest){
        return  atmService.deposit(formParameters.getFirst("cardNumber"), formParameters.getFirst("amount"), httpServletRequest);
    }

    @PostMapping("/withdraw")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String withdraw(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest){
        return  atmService.withdraw(formParameters.getFirst("cardNumber"), formParameters.getFirst("amount"), httpServletRequest);
    }

    @PostMapping("/checkBalance")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String checkBalance(@RequestBody MultiValueMap<String, String> formParameters, HttpServletRequest httpServletRequest){
        return  atmService.checkBalance(formParameters.getFirst("cardNumber"), httpServletRequest);
    }
    @PostMapping("/done")
    @CircuitBreaker(name = "cbinstance", fallbackMethod = "failMethodForAll")
    public String done(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().invalidate();
        return  atmService.done(httpServletRequest);
    }

    public HashMap<String, String> failMethodForFindByCardNumberAndCcv(Exception e) {
        HashMap<String, String> failureStatus = new HashMap<>();
        failureStatus.put("Status","Service is taking longer than expected. Kindly try again later.");
        return failureStatus;
    }
    public String failMethodForAll(Exception e){
        return "The service is not responding. Kindly retry later.";
    }
}
