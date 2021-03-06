package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderResponse;
import com.tenx.ms.retail.order.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "Order", description = "Order Management API")
@RestController("ordersControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Create a new Order")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful order creation"),
                    @ApiResponse(code = 404, message = "Store/Product not found"),
                    @ApiResponse(code = 412, message = "Precondition failure"),
                    @ApiResponse(code = 500, message = "Internal server error")
            }
    )
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderResponse createOrder(
            @ApiParam(name = "storeId", value = "The store id", required = true) @PathVariable("storeId") Long storeId,
            @ApiParam(name = "order", value = "JSON data of the order to be created", required = true) @Validated @RequestBody Order order) {
        return orderService.create(storeId, order);
    }
}
