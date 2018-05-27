package com.spring.service.search;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 索引结构模板
 * Created by chenxizhong on 2018/5/27.
 */
@Data
public class HouseIndexTemplate {

    /**
     * 房源id
     */
    private Long houseId;

    /**
     * 标题
     */
    private String title;

    /**
     * 价格
     */
    private int price;

    /**
     * 房源面积
     */
    private int area;

    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 城市名
     */
    private String cityEnName;

    /**
     * 区域名
     */
    private String regionEnName;

    /**
     * 朝向
     */
    private int direction;

    /**
     * 地铁距离
     */
    private int distanceToSubway;

    /**
     * 地铁名
     */
    private String subwayLineName;

    /**
     * 地铁站名
     */
    private String subwayStationName;

    /**
     * j街道
     */
    private String street;

    /**
     * 小区
     */
    private String district;

    /**
     * 描述
     */
    private String description;

    /**
     *
     * 布局
     */
    private String layoutDesc;

    /**
     *
     */
    private String traffic;

    /**
     * 周边配套
     */
    private String roundService;

    /**
     * 出租方式
     */
    private int rentWay;

    /**
     * 标签
     */
    private List<String> tags;


}
