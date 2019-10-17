package com.imooc.product.service.impl;

import com.imooc.product.common.ProductInfoOutput;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.dto.CartDTO;
import com.imooc.product.enums.ProductStatusEnum;
import com.imooc.product.enums.ResultEnum;
import com.imooc.product.exception.ProductException;
import com.imooc.product.repository.ProductInfoRespository;
import com.imooc.product.service.ProductService;
import com.imooc.product.utils.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRespository productInfoRespository;

    @Autowired
    private AmqpTemplate amqpTemplate;



    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRespository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }
    @Override
    public List<ProductInfo> findByProductIdIn(List<String> productIdList){
        return  productInfoRespository.findByProductIdIn(productIdList);
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        List<ProductInfo> productInfoList = decreaseStockProcess(cartDTOList);

        List<ProductInfoOutput> productInfoOutputList = productInfoList.stream().map(e->{

            ProductInfoOutput productInfoOutput = new ProductInfoOutput();
            BeanUtils.copyProperties(e,productInfoOutput);
            return  productInfoOutput;
        }).collect(Collectors.toList());
        //发送mq消息
        amqpTemplate.convertAndSend("productInfo", JsonUtil.toJson(productInfoOutputList));

    }

    public List<ProductInfo> decreaseStockProcess(List<CartDTO> cartDTOList) {
        List<ProductInfo> productInfoList = new ArrayList<>();

        for (CartDTO cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfoOptional = productInfoRespository.findById(cartDTO.getProductId());
            if(!productInfoOptional.isPresent()){
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = productInfoOptional.get();

            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();

            if(result <0){
                throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
            productInfoRespository.save(productInfo);
            productInfoList.add(productInfo);
        }
        return  productInfoList;
    }
}
