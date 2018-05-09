package com.spring.service.user;

import com.spring.common.base.ServiceResult;
import com.spring.entity.User;
import com.spring.web.dto.UserDTO;

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

    ServiceResult<UserDTO> findById(Long userId);
}
