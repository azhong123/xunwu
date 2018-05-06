package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 地铁对应站点
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "subway_station")
public class SubwayStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 所属地铁线id
     */
    @Column(name = "subway_id")
    private Long subwayId;

    /**
     * 站点名称
     */
    @Column(name = "name")
    private String name;


}
