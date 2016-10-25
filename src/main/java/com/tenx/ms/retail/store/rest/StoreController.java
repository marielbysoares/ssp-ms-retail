package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Store", description = "Store Management API")
@RestController
@RequestMapping(RestConstants.VERSION_ONE + "/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "Create a store", authorizations = { @Authorization("ROLE_ADMIN") })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 412, message = "Precondition failure"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResourceCreated<Long> create(@ApiParam(name = "store", value="JSON data of the store to be created", required = true) @Validated @RequestBody Store store) {
        return new ResourceCreated<>(storeService.create(store));
    }

    @ApiOperation(value = "List of Stores")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page", defaultValue = "20"),
    })
    @RequestMapping(method = RequestMethod.GET)
    public List<Store> findAll(Pageable pageable, @RequestParam(value = "name", required = false) Optional<String> name) {
        if (name.isPresent()) {
            return storeService.findAllByName(pageable, name.get());
        } else {
            return storeService.findAll(pageable);
        }
    }

    @ApiOperation(value = "Get a Store")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @RequestMapping(value = "/{storeId:\\d+}", method = RequestMethod.GET)
    public Store getById(@ApiParam(name = "storeId", value = "The id of the requested store", required = true) @PathVariable("storeId") Long storeId) {
        return storeService.getById(storeId);
    }

    @ApiOperation(value = "Delete a Store", authorizations = { @Authorization("ROLE_ADMIN") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteStore(@ApiParam(name = "Store id", value = "The id of the store to be deleted", required = true) @PathVariable Long storeId){
        storeService.delete(storeId);
    }

}
