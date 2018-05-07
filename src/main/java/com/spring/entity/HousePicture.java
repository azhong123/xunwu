package com.spring.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 房源图片
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@Entity
@Table(name = "house_picture")
public class HousePicture {

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
     * 文件名
     */
    @Column(name = "path")
    private String path;

    /**
     * 图片路径
     */
    @Column(name = "cdn_prefix")
    private String cdnPrefix;

    /**
     * 图片宽度
     */
    @Column(name = "width")
    private int width;

    /**
     * 图片高度
     */
    @Column(name = "height")
    private int height;

    /**
     * 所属房屋位置
     */
    @Column(name = "location")
    private String location;

}
