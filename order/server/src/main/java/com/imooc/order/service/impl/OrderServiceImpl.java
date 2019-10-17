package com.imooc.order.service.impl;

import com.imooc.order.dataobject.OrderDetail;
import com.imooc.order.dataobject.OrderMaster;
import com.imooc.order.dto.OrderDTO;
import com.imooc.order.enums.OrderStatusEnum;
import com.imooc.order.enums.PayStatusEnum;
import com.imooc.order.enums.ResultEnum;
import com.imooc.order.exception.OrderException;
import com.imooc.order.repository.OrderDetailRepository;
import com.imooc.order.repository.OrderMasterRespository;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.KeyUtil;
import com.imooc.product.client.ProductClient;
import com.imooc.product.common.DecreaseStockInput;
import com.imooc.product.common.ProductInfoOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRespository orderMasterRespository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO creat(OrderDTO orderDTO) {
        //秒杀逻辑 商品信息存储到redis
        //读redis （判断）
        //减库存并将新值设置进redis
        //多线程并发  加redis锁
        //手动加锁 异常捕捉手动回滚 (redis)加上减去的库存
        //

        //查询商品信息（调用商品服务）
        List<String> productIdList = orderDTO.getOrderDetailList().stream().map(OrderDetail::getProductId).collect(Collectors.toList());

        List<ProductInfoOutput> productInfos = productClient.listForOrder(productIdList);
        //计算总价
        String orderId = KeyUtil.getUniqueKey();

        BigDecimal orderAmount = new BigDecimal(0);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo : productInfos) {
                if(orderDetail.getProductId().equals(productInfo.getProductId())){
                    //单价 * 数量
                    orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
                    BeanUtils.copyProperties(productInfo,orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.getUniqueKey());

                    orderDetailRepository.save(orderDetail);
                }
            }
        }


        //扣库存（调用商品服务）
        List<DecreaseStockInput> cartDTOList = orderDTO.getOrderDetailList().stream().map(e->new DecreaseStockInput(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productClient.decreaseStock(cartDTOList);
        //订单入库
        orderDTO.setOrderId(orderId);
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setPayStatus(PayStatusEnum.WAID.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());

        orderMasterRespository.save(orderMaster);


//        orderDetailRepository.save();
        return orderDTO;
    }

    @Override
    public OrderDTO finish(String orderId) {
        //1.先查询订单
        Optional<OrderMaster> orderMasterOptional = orderMasterRespository.findById(orderId);
        if(!orderMasterOptional.isPresent()){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2.判断订单状态
        OrderMaster orderMaster = orderMasterOptional.get();
        if(OrderStatusEnum.NEW.getCode() != orderMaster.getOrderStatus()){
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //3.修改订单状态完成
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterRespository.save(orderMaster);
        //4.查询订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
