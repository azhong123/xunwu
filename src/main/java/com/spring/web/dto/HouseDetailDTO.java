package com.spring.web.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 房屋详情
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@ToString
public class HouseDetailDTO {

    private String description;

    private String layoutDesc;

    private String traffic;

    private String roundService;

    private int rentWay;

    private Long adminId;

    private String address;

    private Long subwayLineId;

    private Long subwayStationId;

    private String subwayLineName;

    private String subwayStationName;

}
