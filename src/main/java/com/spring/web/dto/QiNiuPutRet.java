package com.spring.web.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 七牛返回数据
 * Created by chenxizhong on 2018/5/6.
 */
@Data
@ToString
public final class QiNiuPutRet {

    /**
     * 图片key
     */
    public String key;

    public String hash;

    /**
     * 图片空间名
     */
    public String bucket;

    public int width;

    public int height;

}
