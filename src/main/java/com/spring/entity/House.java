package com.spring.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 房屋实体
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "house")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 房屋标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 所属管理员id
     */
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 价格
     */
    @Column(name = "price")
    private int price;

    /**
     * 面积
     */
    @Column(name = "area")
    private int area;

    /**
     * 卧室数量
     */
    @Column(name = "room")
    private int room;

    /**
     * 客厅数量
     */
    @Column(name = "parlour")
    private int parlour;

    @Column(name = "bathroom")
    private int bathroom;

    /**
     * 楼层
     */
    @Column(name = "floor")
    private int floor;

    /**
     * 总楼层
     */
    @Column(name = "total_floor")
    private int totalFloor;

    /**
     * 被看次数
     */
    @Column(name = "watch_times")
    private int watchTimes;

    /**
     * 建立年限
     */
    @Column(name = "build_year")
    private int buildYear;

    /**
     * 房屋状态 0-未审核 1-审核通过 2-已出租 3-逻辑删除
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最近数据更新时间
     */
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 城市标记缩写 如 北京bj
     */
    @Column(name = "city_en_name")
    private String cityEnName;

    /**
     * 地区英文简写 如昌平区 cpq
     */
    @Column(name = "region_en_name")
    private String regionEnName;

    /**
     * 街道
     */
    @Column(name = "street")
    private String street;

    /**
     * 所在小区
     */
    @Column(name = "district")
    private String district;

    /**
     * 房屋朝向
     */
    @Column(name = "direction")
    private int direction;

    /**
     * 封面
     */
    @Column(name = "cover")
    private String cover;

    /**
     * 距地铁距离 默认-1 附近无地铁
     */
    @Column(name = "distance_to_subway")
    private int distanceToSubway;

}
