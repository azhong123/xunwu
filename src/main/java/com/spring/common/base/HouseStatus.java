package com.spring.common.base;

/**
 * 房源状态
 * Created by chenxizhong on 2018/5/7.
 */
public enum HouseStatus {

    NOT_AUDITED(0), // 未审核
    PASSES(1), // 审核通过
    RENTED(2), // 已出租
    DELETED(3); // 逻辑删除
    private int value;

    public int getValue() {
        return value;
    }

    HouseStatus(int value) {
        this.value = value;
    }
}
