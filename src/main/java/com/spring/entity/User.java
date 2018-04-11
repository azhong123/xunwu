package com.spring.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chenxizhong on 2018/4/11.
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "name")
    private String name;

    /**
     * 电子邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 电话号码
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 密码
     */
    @Column(name = "password")
    private Long password;

    /**
     * 用户状态 0-正常 1-封禁
     */
    @Column(name = "status")
    private int status;

    /**
     * 用户账号创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 上次更新记录时间
     */
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 头像
     */
    @Column(name = "avatar")
    private String avatar;



}
