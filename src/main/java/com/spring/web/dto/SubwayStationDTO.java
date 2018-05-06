package com.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 地铁站点实体
 * Created by chenxizhong on 2018/5/6.
 */
@Data
public class SubwayStationDTO {

    private Long id;

    /**
     * 所属地铁线id
     */
    @JsonProperty(value = "subway_id")
    private Long subwayId;

    /**
     * 站点名称
     */
    private String name;

}
