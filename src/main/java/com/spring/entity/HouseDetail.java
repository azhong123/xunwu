package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 房屋具体信息
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "house_detail")
public class HouseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 对应house的id
     */
    @Column(name = "house_id")
    private Long houseId;

    /**
     * 详细描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 户型介绍
     */
    @Column(name = "layout_desc")
    private String layoutDesc;

    /**
     * 交通出行
     */
    @Column(name = "traffic")
    private String traffic;

    /**
     * 周边配套
     */
    @Column(name = "round_service")
    private String roundService;

    /**
     * 租赁方式
     */
    @Column(name = "rent_way")
    private int rentWay;

    /**
     * 详细地址
     */
    @Column(name = "address")
    private String detailAddress;

    /**
     * 附近地铁线id
     */
    @Column(name = "subway_line_id")
    private Long subwayLineId;

    /**
     *  附近地铁线站id
     */
    @Column(name = "subway_station_id")
    private Long subwayStationId;

    /**
     * 附近地铁线名称
     */
    @Column(name = "subway_line_name")
    private String subwayLineName;

    /**
     * 附近地铁站名称
     */
    @Column(name = "subway_station_name")
    private String subwayStationName;

}
