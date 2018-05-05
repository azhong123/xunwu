package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by chenxizhong on 2018/5/5.
 */
@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户角色名
     */
    @Column(name = "name")
    private String name;


}
