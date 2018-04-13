package com.spring.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.common.util.MessageSourceUtil;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口同意返回类型
 * Created by chenxizhong on 2018/3/21.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) // 顾虑为空的字段
public class ResultResponse<T> implements Serializable {

    private static final String SUCCESS = "K-000000";

    private String code;

    private String message;

    private T data;

    private ResultResponse(String code){
        this.code = code;
    }

    private ResultResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    private ResultResponse(String code, T data){
        this.code = code;
        this.data = data;
    }

    private ResultResponse(String code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore // json 序列号过滤
    public boolean isSuccess(){
        return this.code.equals(SUCCESS);
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public T getData(){
        return data;
    }

    /**
     * 正确的返回类型
     * @param <T>
     * @return
     */
    public static <T> ResultResponse<T> createBySuccess(){
        return new ResultResponse<T>(SUCCESS, MessageSourceUtil.getMessage(SUCCESS));
    }

    public static <T> ResultResponse<T> createBySuccess(T data){
        return new ResultResponse<T>(SUCCESS, MessageSourceUtil.getMessage(SUCCESS),data);
    }

    /**
     * 错误的返回类型
     */
    public static  <T> ResultResponse<T> createByError(String errorCode, String... args){
        return new ResultResponse<T>(errorCode, MessageSourceUtil.getMessage(errorCode, args, LocaleContextHolder.getLocale()));
    }

    public static <T> ResultResponse<T> createByError(String errorCode, String msg){
        return new ResultResponse<T>(errorCode,msg);
    }

    /**
     * 如果结果只有一个值，用此方法
     * @param key
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ResultResponse<T> createBySuccessKey(String key ,Object obj){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(key,obj);
        return createBySuccess((T)map);
    }

    public static <T> ResultResponse<T> createBySuccessMap(Map map){
        return createBySuccess((T)map);
    }

}
