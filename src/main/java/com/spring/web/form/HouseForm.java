package com.spring.web.form;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 保存房屋信息 接受收据
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@ToString
public class HouseForm {

    private Long id;

    @NotNull(message = "大标题不允许为空!")
    @Size(min = 1, max = 30, message = "标题长度必须在1~30之间")
    private String title;

    @NotNull(message = "必须选中一个城市")
    @Size(min = 1, message = "非法的城市")
    private String cityEnName;

    @NotNull(message = "必须选中一个地区")
    @Size(min = 1, message = "非法的地区")
    private String regionEnName;

    @NotNull(message = "必须填写街道")
    @Size(min = 1, message = "非法的街道")
    private String street;

    @NotNull(message = "必须填写小区")
    private String district;

    @NotNull(message = "详细地址不允许为空!")
    @Size(min = 1, max = 30, message = "详细地址长度必须在1~30之间")
    private String detailAddress;

    @NotNull(message = "必须填写卧室数量")
    @Min(value = 1, message = "非法的卧室数量")
    private Integer room;

    /**
     * 客厅数量
     */
    private int parlour;

    @NotNull(message = "必须填写所属楼层")
    private Integer floor;

    @NotNull(message = "必须填写总楼层")
    private Integer totalFloor;

    @NotNull(message = "必须填写房屋朝向")
    private Integer direction;

    @NotNull(message = "必须填写建筑起始时间")
    @Min(value = 1900, message = "非法的建筑起始时间")
    private Integer buildYear;

    @NotNull(message = "必须填写面积")
    @Min(value = 1)
    private Integer area;

    @NotNull(message = "必须填写租赁价格")
    @Min(value = 1)
    private Integer price;

    @NotNull(message = "必须选中一个租赁方式")
    @Min(value = 0)
    @Max(value = 1)
    private Integer rentWay;

    /**
     * 附近地铁线id
     */
    private Long subwayLineId;

    /**
     * 附近地铁站id
     */
    private Long subwayStationId;

    /**
     * 距地铁距离 默认-1 附近无地铁
     */
    private int distanceToSubway = -1;

    /**
     * 户型介绍
     */
    private String layoutDesc;

    /**
     * 周边配套
     */
    private String roundService;

    /**
     * 交通出行
     */
    private String traffic;

    /**
     * 详细描述
     */
    @Size(max = 255)
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 房屋标签
     */
    private List<String> tags;

    /**
     * 房屋照片
     */
    private List<PhotoForm> photos;

}
