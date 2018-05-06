package com.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 地铁返回实体
 * Created by chenxizhong on 2018/5/6.
 */
@Data
public class SubwayDTO {

    private Long id;

    /**
     * 线路名
     */
    private String name;

    /**
     * 所属城市英文名缩写
     */
    @JsonProperty(value = "city_en_name")
    private String cityEnName;
}
