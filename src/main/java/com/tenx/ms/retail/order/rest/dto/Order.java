package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.validation.constraints.Email;
import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("Order")
@Data
public class Order {
    @ApiModelProperty(value="Order id", readOnly = true)
    private Long orderId;

    @ApiModelProperty(value="Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Order date", readOnly = true)
    private LocalDateTime orderDate;

    @ApiModelProperty(value = "Order status")
    private OrderStatusEnum status;

    @ApiModelProperty(value = "First name", required = true, example = "Jane")
    @Pattern(regexp = "\\A[a-zA-Z]+\\z", message = "First name must be alpha only")
    @NotBlank
    private String firstName;

    @ApiModelProperty(value = "Last name", required = true, example = "Doe")
    @Pattern(regexp = "\\A[a-zA-Z]+\\z", message = "Last name must be alpha only")
    @NotBlank
    private String lastName;

    @ApiModelProperty(value = "Email", required = true, example = "email@email.com")
    @Email
    @NotBlank
    private String email;

    @ApiModelProperty(value = "Phone number", required = true, example = "7869700939")
    @PhoneNumber
    @NotBlank
    private String phone;

    @ApiModelProperty(value = "Order Products", required = true)
    @NotEmpty
    @Valid
    private List<OrderProduct> products;
}
