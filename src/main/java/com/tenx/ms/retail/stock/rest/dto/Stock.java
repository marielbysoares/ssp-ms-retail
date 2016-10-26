package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("Stock")
@Data
public class Stock {
    @ApiModelProperty(value = "Store Id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Product Id", readOnly = true)
    private Long productId;

    @ApiModelProperty(value = "Stock count", required = true, example = "5")
    @NotNull
    @Min(value = 0)
    private Integer count;
}
