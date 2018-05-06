package com.spring.common.base;

import lombok.Data;

import java.util.List;

/**
 * 集合统一返回对象
 * Created by chenxizhong on 2018/5/6.
 */
@Data
public class ServiceMultiResult<T> {

    private long total;
    private List<T> result;

    public ServiceMultiResult(long total, List<T> result) {
        this.total = total;
        this.result = result;
    }

    public int getResultSize(){
        if(this.result == null){
            return 0;
        }
        return this.result.size();
    }
}
