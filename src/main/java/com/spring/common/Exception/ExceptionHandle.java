package com.spring.common.Exception;


import com.spring.common.base.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;

/**
 * 捕获异常的 类
 * Created by chenxizhong on 2018/3/22.
 */
@ControllerAdvice
public class ExceptionHandle {

    /**
     * 虽然我们将异常捕获，不抛出，但如果我们要看不是我们自定义的异常，要看哪边出错了，
     * 这个时候我们可以通过日志的方式将错误打印出来
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     *
     * @param request
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ResultResponse signatureExceptionHandler(HttpServletRequest request, SignatureException ex) {
        LOGGER.error("do [{}] on [{}] failed. exMsg:{}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("queryString:{}, parameterMap: {}", request.getQueryString(), ex);
        }

        return ResultResponse.createByError("K-000001", ex.getLocalizedMessage());
    }

    /**
     * 上传图片异常处理
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = MultipartException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ResponseBody
    public ResultResponse serviceExceptionHandler(HttpServletRequest request, MultipartException ex) throws Exception{
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("queryString:{}, parameterMap: {}", request.getQueryString(), ex);
        }
        return ResultResponse.createByError("K-010002");
    }

    /**
     * 自定义异常扑获
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultResponse handException(HttpServletRequest request, ServiceException ex){
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        LOGGER.error("do [{}] on [{}] failed. exMsg:{}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("queryString:{}, parameterMap: {}", request.getQueryString(), ex);
        }

        return ResultResponse.createByError(ex.getCode(),ex.getMessage());

    }

    /**
     * 资源未找到
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResultResponse serviceExceptionHandler(HttpServletRequest request, ResourceNotFoundException ex) throws
            Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        LOGGER.error("do [{}] on [{}] failed. exMsg:{}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("queryString:{}, parameterMap: {}", request.getQueryString(), ex);
        }

        return ResultResponse.createByError("K-000001",ex.getLocalizedMessage());
    }

    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<ResultResponse> generalHttpExceptionHandler(HttpServletRequest request, HttpException ex)
            throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        LOGGER.error("do [{}] on [{}] failed. exMsg:{}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("queryString:{}, parameterMap: {}", request.getQueryString(), ex);
        }

        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body((ResultResponse) ResultResponse.createByError("K-000001",ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultResponse defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        LOGGER.error("do [{}] on [{}] failed. exMsg:{}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("queryString:{}, parameterMap: {}", request.getQueryString(), ex);
        }

        return ResultResponse.createByError("K-000001",ex.getLocalizedMessage());
    }


}
