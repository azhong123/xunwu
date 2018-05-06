package com.spring.service.house;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.InputStream;

/**
 * 七牛云服务
 * Created by chenxizhong on 2018/5/6.
 */
public interface IQiNiuService {

    /**
     * 文件上传
     * @param file
     * @return
     * @throws QiniuException
     */
    Response uploadFile(File file) throws QiniuException;

    /**
     * 流上传图片
     * @param inputStream
     * @return
     * @throws QiniuException
     */
    Response uploadFile(InputStream inputStream) throws QiniuException;

    /**
     * 删除图片
     * @param key
     * @return
     * @throws QiniuException
     */
    Response delete(String key) throws QiniuException;


}
