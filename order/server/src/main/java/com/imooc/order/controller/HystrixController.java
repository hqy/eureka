package com.imooc.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@DefaultProperties(defaultFallback = "defaultFallBack")
public class HystrixController {


    //超时配置
//    @HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")})

    //服务熔断
    @HystrixCommand(commandProperties = {@HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//设置熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")})

//    @HystrixCommand
    @RequestMapping("/getProductInfoList")
    public String getProductInfoList(@RequestParam("number") Integer number){

        if(number %2 == 0){
            return "success";
        }
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject("http://127.0.0.1:8003/product/listForOrder", Arrays.asList("157875196366160022"),String.class);
    }

    public String fallback(){
        return "太拥挤了,请稍后再试";
    }
    public String defaultFallBack(){
        return "默认提示：太拥挤了,请稍后再试~~";
    }

}
