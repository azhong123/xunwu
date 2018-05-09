package com.spring.web.dto;

import lombok.Data;

import java.util.Date;

/**
 * 预约看房信息
 * Created by chenxizhong on 2018/5/8.
 */
@Data
public class HouseSubscribeDTO {

    private Long id;

    /**
     * 房源id
     */
    private Long houseId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 管理员id
     */
    private Long adminId;

    /**
     * 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
     */
    private int status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 预约时间
     */
    private Date orderTime;

    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 用户描述
     */
    private String desc;

}
