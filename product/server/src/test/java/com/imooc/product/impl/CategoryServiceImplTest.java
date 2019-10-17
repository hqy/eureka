package com.imooc.product.impl;

import com.imooc.product.ProductApplicationTest;
import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.service.CategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryServiceImplTest  extends ProductApplicationTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> list = categoryService.findByCategoryTypeIn(Arrays.asList(11, 22));

        Assert.assertTrue(CollectionUtils.isNotEmpty(list));
    }
}