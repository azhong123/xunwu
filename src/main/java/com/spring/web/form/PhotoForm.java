package com.spring.web.form;

import lombok.Data;

/**
 * 房屋照片
 * Created by chenxizhong on 2018/5/6.
 */
@Data
public class PhotoForm {

    /**
     * 照片路径
     */
    private String path;

    /**
     * 照片宽度
     */
    private int width;

    /**
     * 照片高度
     */
    private int height;

}
