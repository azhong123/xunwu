package com.spring.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 预约看房实体
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "house_subscribe")
public class HouseSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 房源id
     */
    @Column(name = "house_id")
    private Long houseId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 房源发布者id
     */
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
     */
    @Column(name = "status")
    private int status;

    /**
     * 数据创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 预约时间
     */
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 联系电话
     */
    @Column(name = "telephone")
    private String telephone;

    /**
     * 用户描述
     * 踩坑 desc为MySQL保留字段 需要加转义
     */
    @Column(name = "`desc`")
    private String desc;

}
