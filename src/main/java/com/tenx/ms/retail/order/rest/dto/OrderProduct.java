package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("Order Product")
@Data
public class OrderProduct {
    @ApiModelProperty(name = "Order product id", readOnly = true)
    private Long orderProductId;

    @ApiModelProperty(name = "Product Id", required = true)
    private Long productId;

    @ApiModelProperty(name = "Order Product count", required = true, example = "10")
    @NotNull
    @Min(value = 0)
    private Integer count;
}
