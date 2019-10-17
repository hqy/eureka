package com.imooc.product.service;

import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.dto.CartDTO;

import java.util.List;

public interface ProductService {

    List<ProductInfo> findUpAll();


    List<ProductInfo> findByProductIdIn(List<String> productIdList);

    void decreaseStock(List<CartDTO> cartDTOList);
}
