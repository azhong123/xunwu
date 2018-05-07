package com.spring.common.base;

import lombok.Data;

/**
 * Datatables响应结构
 * Created by chenxizhong on 2018/5/7.
 */
@Data
public class ApiDataTableResponse extends ApiResponse {

    private int draw;
    private long recordsTotal;
    private long recordsFiltered;

    public ApiDataTableResponse(ApiResponse.Status status) {
        this(status.getCode(), status.getStandarMessage(), null);
    }

    public ApiDataTableResponse(int code, String message, Object data) {
        super(code, message, data);
    }

}
