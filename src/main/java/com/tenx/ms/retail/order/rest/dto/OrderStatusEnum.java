package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.rest.BaseValueNameEnum;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "Order Status")
public enum OrderStatusEnum implements BaseValueNameEnum<OrderStatusEnum> {
    ORDERED(1, "ORDERED"),
    PACKING(2, "PACKING"),
    SHIPPED(3, "SHIPPED"),
    INVALID(4, "INVALID");

    private int value;
    private String label;

    OrderStatusEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toJson() {
        return label;
    }

    @Override
    public OrderStatusEnum getInvalidEnum() {
        return INVALID;
    }

    @Override
    public int getValue() {
        return value;
    }
}