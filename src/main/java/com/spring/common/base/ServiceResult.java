package com.spring.common.base;

import lombok.Data;

/**
 * 服务接口通用结构
 * Created by chenxizhong on 2018/5/6.
 */
@Data
public class ServiceResult<T> {

    private boolean success;

    private String message;

    private T result;

    public ServiceResult(boolean success){
        this.success = success;
    }

    public ServiceResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }

    public ServiceResult(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess(){
        return success;
    }

    public static <T> ServiceResult<T> success(){
        return new ServiceResult<T>(true);
    }

    public static <T> ServiceResult<T> of(T result){
        ServiceResult<T> serviceResult = new ServiceResult<T>(true);
        serviceResult.setResult(result);
        return serviceResult;
    }

    public static <T> ServiceResult<T> notFound(){
        return new ServiceResult<T>(false,Message.NOT_FOUND.getValue());
    }

    public enum Message{
        NOT_FOUND("Not Found Resource!"),
        NOT_LOGIN("User Not Login!");

        private String value;

        public String getValue() {
            return value;
        }

        Message(String value) {
            this.value = value;
        }

    }
}
