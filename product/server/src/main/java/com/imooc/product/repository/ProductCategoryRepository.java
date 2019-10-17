package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductCategory;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProductCategoryRepository extends
        Repository<ProductCategory,String> {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

}
