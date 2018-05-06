package com.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

/**
 * Created by chenxizhong on 2018/5/6.
 */
@Data
public class SupportAddressDTO {

    private Long id;

    /**
     * 上一级行政单位名
     */
    @JsonProperty(value = "belong_to")
    private String belongTo;

    /**
     * 行政单位英文名缩写
     */
    @JsonProperty(value = "en_name")
    private String enName;

    /**
     * 行政单位中文名
     */
    @JsonProperty(value = "cn_name")
    private  String cnName;

    /**
     * 行政级别 市-city 地区-region
     */
    private String level;

    /**
     * 百度地图经度
     */
    @JsonProperty(value = "baidu_map_lng")
    private double baiduMapLng;

    /**
     * 百度地图纬度
     */
    @JsonProperty(value = "baidu_map_lat")
    private double baiduMapLat;

}
