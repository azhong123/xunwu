package com.spring.service.house.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.spring.ApplicationTests;
import com.spring.service.house.IQiNiuService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by chenxizhong on 2018/5/6.
 */
public class QiNiuServiceImplTest extends ApplicationTests{


    @Autowired
    private IQiNiuService qiNiuService;

    @Test
    public void uploadFile() {
        String fileName = "H://IdeaSpace/xunwu/tmp/xiaoqian.jpeg";
        File file = new File(fileName);
        // 文件是否存在
        Assert.assertTrue(file.exists());

        Response response = null;
        try {
            response = qiNiuService.uploadFile(file);
            Assert.assertTrue(response.isOK());
        } catch (QiniuException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void delete() throws Exception {
        Response response = qiNiuService.delete("FvyNceBAaZF6TBh6OZpcEKlhuACG");
        Assert.assertTrue(response.isOK());

    }

}