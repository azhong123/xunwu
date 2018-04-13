package com.spring.common.Exception;

import com.spring.common.base.ResultResponse;
import com.spring.common.util.MessageSourceUtil;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 自定义异常
 * Created by chenxizhong on 2018/2/24.
 */
public class ServiceException extends RuntimeException {

    private String code;

    public ServiceException(String errorCode, String... args){
        this(errorCode, MessageSourceUtil.getMessage(errorCode, args, LocaleContextHolder.getLocale()));
    }

    public ServiceException(ResultResponse resultResponse) {
        super(resultResponse.getMessage());
        this.code = resultResponse.getCode();
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
