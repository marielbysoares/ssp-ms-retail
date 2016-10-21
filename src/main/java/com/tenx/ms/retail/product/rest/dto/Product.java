package com.tenx.ms.retail.product.rest.dto;

import com.tenx.ms.commons.validation.constraints.DollarAmount;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ApiModel("Product")
@Data
public class Product {
    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long productId;

    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Product name", required = true, example = "Product name")
    @NotNull
    private String name;

    @ApiModelProperty(value = "Product description", example = "Product description")
    private String description;

    @ApiModelProperty(value = "Product Stock Keeping Unit (SKU)", required = true, example = "A76BC28F")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "SKU must be alphanumeric")
    @Size(min = 5, max = 10)
    @NotNull
    private String sku;

    @ApiModelProperty(value = "Product price", required = true, example = "44.99")
    @DollarAmount
    @NotNull
    private BigDecimal price;
}
