package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 房屋标签
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "house_tag")
public class HouseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 房屋id
     */
    @Column(name = "house_id")
    private Long houseId;

    /**
     * 标签名称
     */
    @Column(name = "name")
    private String name;

    public HouseTag() {
    }

    public HouseTag(Long houseId, String name) {
        this.houseId = houseId;
        this.name = name;
    }


}
