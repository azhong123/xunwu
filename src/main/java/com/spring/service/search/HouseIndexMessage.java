package com.spring.service.search;

import lombok.Data;

/**
 * kafka 消息结构体
 * Created by chenxizhong on 2018/5/29.
 */
@Data
public class HouseIndexMessage {

    public static final String INDEX = "index";

    public static final String REMOVE = "remove";

    public static final int MAX_RETRY = 3;

    private Long houseId;

    /**
     * 操作类型 创建 还是移除
     */
    private String operation;

    private int retry = 0;

    // 防止序列化失败
    public HouseIndexMessage() {
    }

    public HouseIndexMessage(Long houseId, String operation, int retry) {
        this.houseId = houseId;
        this.operation = operation;
        this.retry = retry;
    }
}
