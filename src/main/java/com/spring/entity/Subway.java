package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 地铁
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "subway")
public class Subway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 线路名
     */
    @Column(name = "name")
    private String name;

    /**
     * 所属城市英文名缩写
     */
    @Column(name = "city_en_name")
    private String cityEnName;

}
