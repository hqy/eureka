package com.imooc.order.controller;


import com.imooc.order.VO.ResultVO;
import com.imooc.order.VO.ResultVOUtil;
import com.imooc.order.converter.OrderForm2OrderDTO;
import com.imooc.order.dto.OrderDTO;
import com.imooc.order.enums.ResultEnum;
import com.imooc.order.exception.OrderException;
import com.imooc.order.form.OrderForm;
import com.imooc.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     *1.参数校验
     *2.查询商品信息（调用商品服务）
     *3.计算总价
     *4.扣库存（调用商品服务）
     *5.订单入库
     */

    @PostMapping("/create")
    public ResultVO<Map<String,String>> creat(@Valid OrderForm orderForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确，orderForm{}",orderForm);
            throw new OrderException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        //orderForm  -- > orderDto
        OrderDTO orderDTO = OrderForm2OrderDTO.covert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车信息为空");
            throw  new OrderException(ResultEnum.CART_EMPTY);
        }

        OrderDTO result = orderService.creat(orderDTO);

        Map<String,String> map = new HashMap<>();
        map.put("orderId",result.getOrderId());

        return ResultVOUtil.success(map);
    }


    /**
     * 完结订单
     * order/finish
     */

    @PostMapping("/finish")
    public ResultVO<OrderDTO> creat(@RequestParam("orderId") String orderId){

        return ResultVOUtil.success(orderService.finish(orderId));
    }
}
