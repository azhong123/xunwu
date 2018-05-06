package com.spring.common.base;

/**
 * api 格式封装
 * Created by chenxizhong on 2018/5/5.
 */
public class ApiResponse {

    private int code;

    private String message;

    private Object data;

    private boolean more;

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse() {
        this.code = Status.SUCCESS.getCode();
        this.message = Status.SUCCESS.getStandarMessage();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isMore() {
        return more;
    }

    // 构建常用的方法
    public static ApiResponse ofMessage(int code,String message){
        return new ApiResponse(code,message,null);
    }

    public static ApiResponse ofSuccess(Object data){
        return new ApiResponse(Status.SUCCESS.getCode(),Status.SUCCESS.getStandarMessage(),data);
    }

    public static ApiResponse ofStatus(Status status){
        return new ApiResponse(status.getCode(),status.getStandarMessage(),null);
    }

    public enum Status{
        SUCCESS(200,"OK"),
        BAD_REQUEST(400,"Bad Request"),
        NOT_FOUND(404, "NOT FOUND"),
        INTERNAL_SERVER_ERROR(500,"Unknow Internal Error"),
        NOT_VALID_PARAMS(40005,"Not Valid Params"),
        NOT_SUPPORTED_OPERATION(40006,"Operation Not Supported"),
        NOT_LOGIN(50000,"Not Login");

        private int code;

        private String standarMessage;

        Status(int code, String standarMessage) {
            this.code = code;
            this.standarMessage = standarMessage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandarMessage() {
            return standarMessage;
        }

        public void setStandarMessage(String standarMessage) {
            this.standarMessage = standarMessage;
        }
    }
}
