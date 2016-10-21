package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "Stock", description = "Product Management API")
@RestController("stockControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Add/Update Product Quantity")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful upsert of Stock"),
                    @ApiResponse(code = 412, message = "Precondition failure"),
                    @ApiResponse(code = 500, message = "Internal server error")
            }
    )
    @RequestMapping(value = {"/{storeId:\\d+}/{productId:\\d+}"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateStock(
            @ApiParam(name = "storeId", value = "The store id") @PathVariable() Long storeId,
            @ApiParam(name = "productId", value = "The product id") @PathVariable() Long productId,
            @ApiParam(name = "stock", value = "JSON data of the stock to be added") @Validated @RequestBody Stock stock) {
        stockService.upsert(storeId, productId, stock);
    }

    @ApiOperation(value = "Get Stock")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Stock not found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(value = "/{storeId:\\d+}/{productId:\\d+}", method = RequestMethod.GET)
    public Stock getById(
            @ApiParam(name = "productId", value = "The id of the requested product stock", required = true) @PathVariable("productId") Long productId) {
        return stockService.findByProductId(productId);
    }

}
