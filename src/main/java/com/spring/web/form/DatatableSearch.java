package com.spring.web.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 房源列表请求数据
 * Created by chenxizhong on 2018/5/7.
 */
@Data
public class DatatableSearch {

    /**
     * Datatables要求回显字段
     */
    private int draw;

    /**
     * Datatables规定分页字段
     * 开始页
     */
    private int start;

    /**
     * 每页多少数据
     */
    private int length;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间的最小时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeMin;

    /**
     * 创建时间的 最大时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeMax;

    /**、
     * 城市
     */
    private String city;

    /**
     * 标题
     */
    private String title;

    /**
     *
     */
    private String direction;

    /**
     *
     */
    private String orderBy;

}
