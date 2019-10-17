package com.imooc.user.controller;

import ch.qos.logback.core.util.TimeUtil;
import com.imooc.user.VO.ResultVO;
import com.imooc.user.VO.ResultVOUtil;
import com.imooc.user.constant.CookieConstant;
import com.imooc.user.constant.RedisConstant;
import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.enums.ResultEnum;
import com.imooc.user.enums.RoleEnum;
import com.imooc.user.service.UserService;
import com.imooc.user.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid, HttpServletRequest request, HttpServletResponse response){

        //1.查
        UserInfo userInfo = userService.findByOpenid(openid);
        if(null == userInfo){
            return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
        }
        //2.判断角色
        if(RoleEnum.BUYER.getCode() != userInfo.getRole().intValue()){
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }
        //3.设置cookie
        CookieUtil.set(response, CookieConstant.OPENID,openid,CookieConstant.expire);
        return ResultVOUtil.success();
    }

    @GetMapping("/seller")
    public ResultVO seller(@RequestParam("openid") String openid, HttpServletRequest request, HttpServletResponse response){

        //1.查
        UserInfo userInfo = userService.findByOpenid(openid);
        if(null == userInfo){
            return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
        }
        //2.判断角色
        if(RoleEnum.SELLER.getCode() != userInfo.getRole().intValue()){
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }
        //判断是否已经登录
        Cookie cookie = CookieUtil.get(request,CookieConstant.TOKEN);
        if(null != cookie &&
                StringUtils.isNotEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN,cookie.getValue())))){

            return ResultVOUtil.success();
        }
        //3.redis 写数据
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.expire;
        stringRedisTemplate.opsForValue().set(
                String.format(RedisConstant.TOKEN,token),
                openid,
                expire,
                TimeUnit.SECONDS);
        //4.cookie写数据
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.expire);
        return ResultVOUtil.success();
    }

}
