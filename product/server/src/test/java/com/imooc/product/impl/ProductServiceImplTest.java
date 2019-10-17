package com.imooc.product.impl;

import com.imooc.product.ProductApplicationTest;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.dto.CartDTO;
import com.imooc.product.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductServiceImplTest extends ProductApplicationTest {
    @Autowired
    private ProductService productService;


    @Test
    public void findUpAll() {
        List<ProductInfo> list = productService.findUpAll();

        Assert.assertTrue(list.size()>0);
    }
    @Test
    public void findByProductIdIn() {
        List<ProductInfo> list = productService.findByProductIdIn(Arrays.asList("157875196366160022","157875227953464068"));

        Assert.assertTrue(list.size()>0);
    }

    @Test
    public void decreaseStock() {
        CartDTO cartDTO = new CartDTO("157875196366160022",2);
        productService.decreaseStock(Arrays.asList(cartDTO));
    }
}