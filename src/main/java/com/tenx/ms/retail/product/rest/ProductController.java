package com.tenx.ms.retail.product.rest;


import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.store.rest.dto.Store;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Product", description = "Product Management API")
@RestController("productControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Create a product")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Product created"),
        @ApiResponse(code = 412, message = "Precondition Failure"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{storeId:\\d+}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResourceCreated<Long> create(
        @PathVariable("storeId") Long storeId,
        @ApiParam(name = "product", value="JSON data of the product to be created", required = true) @Validated @RequestBody Product product) {
        return new ResourceCreated<>(productService.create(storeId, product));
    }

    @ApiOperation(value = "List of Products by Store")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404, message = "Store Not found"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page", defaultValue = "20"),
    })
    @RequestMapping(value = "/{storeId:\\d+}", method = RequestMethod.GET)
    public List<Store> findAllByStoreId(Pageable pageable,
        @ApiParam(name = "storeId", value = "The id of the requested store", required = true) @PathVariable("storeId") Long storeId) {
        return productService.findAllByStoreId(pageable, storeId);
    }

    @ApiOperation(value = "Get a Product")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404, message = "Product not found"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(value = "/{storeId:\\d+}/{productId:\\d+}", method = RequestMethod.GET)
    public Product getById(
        @ApiParam(name = "storeId", value = "The id of the requested store", required = true) @PathVariable("storeId") Long storeId,
        @ApiParam(name = "productId", value = "The id of the requested product", required = true) @PathVariable("productId") Long productId) {
        return productService.getByStoreIdAndProductId(storeId, productId);
    }
}
