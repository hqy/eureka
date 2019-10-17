package com.imooc.order.controller;

import com.imooc.product.client.ProductClient;
import com.imooc.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@Slf4j
public class ClientController {

    @Autowired
    private ProductClient productClient;


    @RequestMapping("/getProductList")
    public String listForOrder(){
        List<ProductInfoOutput> productInfos = productClient.listForOrder(Arrays.asList("164103465734242707"));
        log.info("response={}",productInfos);
        return  "ok";
    }

}