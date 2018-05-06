package com.spring.service.user;

import com.spring.entity.User;

/**
 * 用户service
 * Created by chenxizhong on 2018/5/5.
 */
public interface IUserService {

    /**
     * 通过用户名查找用户
     * @param name
     * @return
     */
    User findUserByName(String name);
}
