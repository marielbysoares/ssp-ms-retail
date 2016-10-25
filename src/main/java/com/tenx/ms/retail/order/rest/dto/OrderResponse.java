package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@ApiModel("Order Response")
@Data
@AllArgsConstructor
public class OrderResponse {
    @ApiModelProperty(value="Order id", readOnly = true)
    private Long orderId;

    @ApiModelProperty(value = "Order status", example = "ORDERED")
    private OrderStatusEnum status;

    @ApiModelProperty(value = "Order Backordered items", required = true)
    private List<OrderProduct> backorderedItems;
}
