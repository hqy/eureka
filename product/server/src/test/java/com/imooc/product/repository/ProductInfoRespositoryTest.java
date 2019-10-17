package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRespositoryTest {
    @Autowired
    private  ProductInfoRespository productInfoRespository;

    @Test
    public void findByProductStatus(){

        List<ProductInfo> list = productInfoRespository.findByProductStatus(0);

        Assert.assertTrue(list.size()>0);
    }

    @Test
    public  void findByProductIdIn(){
        List<ProductInfo> list = productInfoRespository.findByProductIdIn(Arrays.asList("157875196366160022","157875227953464068"));
        Assert.assertTrue(list.size()>0);
    }
}