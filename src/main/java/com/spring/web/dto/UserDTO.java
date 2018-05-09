package com.spring.web.dto;

import lombok.Data;

/**
 * Created by chenxizhong on 2018/5/8.
 */
@Data
public class UserDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     *
     */
    private String avatar;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

}
