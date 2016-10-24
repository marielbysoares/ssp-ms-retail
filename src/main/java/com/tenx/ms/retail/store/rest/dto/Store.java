package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("Store")
@Data
public class Store {
    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Store name", required = true, example = "Store name")
    @NotBlank
    private String name;
}
