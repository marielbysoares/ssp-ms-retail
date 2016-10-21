package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.validation.constraints.Email;
import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@ApiModel("Order")
@Data
public class Order {
    @ApiModelProperty(value="Order id", readOnly = true)
    private Long orderId;

    @ApiModelProperty(value="Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Order date", readOnly = true)
    private Date orderDate;

    @ApiModelProperty(value = "Order status")
    private OrderStatusEnum status;

    @ApiModelProperty(value = "First name", required = true, example = "Jane")
    @Pattern(regexp = "\\A[a-zA-Z]+\\z", message = "First name must be alpha only")
    @NotNull
    private String firstName;

    @ApiModelProperty(value = "Last name", required = true, example = "Doe")
    @Pattern(regexp = "\\A[a-zA-Z]+\\z", message = "Last name must be alpha only")
    @NotNull
    private String lastName;

    @ApiModelProperty(value = "Email", required = true, example = "email@email.com")
    @Email
    @NotNull
    private String email;

    @ApiModelProperty(value = "Phone number", required = true, example = "7869700939")
    @PhoneNumber
    @NotNull
    private String phone;

    @ApiModelProperty(value = "Order Products", required = true)
    @Size(min = 1)
    @NotNull
    @Valid
    private List<OrderProduct> products;
}
