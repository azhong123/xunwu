package com.spring.common.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * web错误 全局配置
 * Created by chenxizhong on 2018/5/5.
 */
@Controller
public class AppErrorController  implements ErrorController{

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    public AppErrorController(ErrorAttributes errorAttributes){
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping(value = ERROR_PATH,produces = "text/html")
    public String errorPageHandler(HttpServletRequest request, HttpServletResponse response){
        int status = response.getStatus();
        switch (status){
            case 403:
                return "403";
            case 404:
                return "404";
            case 500:
                return "500";
        }

        return "index";
    }

    /**
     * 除 web 以外的错误信息 比如 json / xml
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ApiResponse errorApiHandler(HttpServletRequest request){
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        // 获取错误信息
        Map<String,Object> attr = this.errorAttributes.getErrorAttributes(requestAttributes,false);

        int status = getStatus(request);
        return ApiResponse.ofMessage(status,String.valueOf(attr.getOrDefault("message","error")));
    }

    /**
     * 获取请求的 状态值
     * @param request
     * @return
     */
    private int getStatus(HttpServletRequest request){
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(status != null){
            return status;
        }
        return 500;
    }
}
