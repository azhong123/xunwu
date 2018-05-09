package com.spring.web.form;

import lombok.Data;

/**
 * 租房请求参数结构体
 * Created by chenxizhong on 2018/5/8.
 */
@Data
public class RentSearch {

    /**
     * 城市 英文简写
     */
    private String cityEnName;

    /**
     * 区域 英文简写
     */
    private String regionEnName;

    /**
     * 价格区间
     */
    private String priceBlock;


    /**
     * 面积区间
     */
    private String areaBlock;

    /**
     * 房间数量
     */
    private int room;

    /**
     * 朝向
     */
    private int direction;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 出租方式 合租 or 整租
     */
    private int rentWay = -1;

    /**
     * 排序字段
     */
    private String orderBy = "lastUpdateTime";

    /**
     * 降序 or 升序
     */
    private String orderDirection = "desc";

    /**
     * 分页参数
     */
    private int start = 0;

    /**
     * 分页每页数量
     */
    private int size = 5;

    public int getSize() {
        if (this.size < 1) {
            return 5;
        } else if (this.size > 100) {
            return 100;
        } else {
            return this.size;
        }
    }

    public int getRentWay() {
        if (rentWay > -2 && rentWay < 2) {
            return rentWay;
        } else {
            return -1;
        }
    }

}
