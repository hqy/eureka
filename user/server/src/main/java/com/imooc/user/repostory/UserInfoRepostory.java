package com.imooc.user.repostory;

import com.imooc.user.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepostory extends JpaRepository<UserInfo,String> {

    UserInfo findByOpenid(String openid);
}
