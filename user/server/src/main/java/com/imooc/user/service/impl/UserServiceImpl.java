package com.imooc.user.service.impl;

import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.repostory.UserInfoRepostory;
import com.imooc.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional

public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoRepostory userInfoRepostory;

    @Override
    public UserInfo findByOpenid(String openid) {
        return userInfoRepostory.findByOpenid(openid);
    }
}
