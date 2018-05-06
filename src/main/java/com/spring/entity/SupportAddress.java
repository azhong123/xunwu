package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 城市行政级别
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "support_address")
public class SupportAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 上一级行政单位名
     */
    @Column(name = "belong_to")
    private String belongTo;

    /**
     * 行政单位英文名缩写
     */
    @Column(name = "en_name")
    private String enName;

    /**
     * 行政单位中文名
     */
    @Column(name = "cn_name")
    private  String cnName;

    /**
     * 行政级别 市-city 地区-region
     */
    @Column(name = "level")
    private String level;

    /**
     * 百度地图经度
     */
    @Column(name = "baidu_map_lng")
    private double baiduMapLng;

    /**
     * 百度地图纬度
     */
    @Column(name = "baidu_map_lat")
    private double baiduMapLat;

    /**
     * 行政级别定义
     */
    public enum Level{
        CITY("city"),
        REGION("region");

        private String value;

        Level(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Level of(String value){
            for (Level level : Level.values()) {
                if(level.getValue().equals(value)){
                    return level;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
