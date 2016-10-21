package com.tenx.ms.retail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.BaseIntegrationTest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class BaseRetailIntegrationTest extends BaseIntegrationTest {
    protected final static String API_VERSION = RestConstants.VERSION_ONE;
    protected static final Long INVALID_ID  = Long.valueOf(9L);

    protected static String STORES_REQUEST_URI   = "%s" + API_VERSION + "/stores/";
    protected static String PRODUCTS_REQUEST_URI = "%s" + API_VERSION + "/products/%s/";
    protected static String STOCK_REQUEST_URI   = "%s" + API_VERSION + "/stock/%s/%s/";
    protected static String ORDERS_REQUEST_URI  = "%s" + API_VERSION + "/orders/%s/";

    @Value("classpath:store/store_success.json")
    private File storeSuccessRequest;

    @Value("classpath:product/product_success.json")
    private File productSuccessRequest;

    protected Long createStore() {
        try {
            ResponseEntity<String> response = getStringResponse(String.format(STORES_REQUEST_URI, getBasePath()), FileUtils.readFileToString(storeSuccessRequest), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect for create store.", HttpStatus.OK, response.getStatusCode());
            ResourceCreated<Long> created = mapper.readValue(response.getBody(), new TypeReference<ResourceCreated<Long>>() {});
            return created.getId();
        } catch (IOException e) {
            fail(e.getMessage());
            return null;
        }
    }

    protected Long createProduct(Long storeId) {
        try {
            ResponseEntity<String> response = getStringResponse(String.format(PRODUCTS_REQUEST_URI, getBasePath(), storeId), FileUtils.readFileToString(productSuccessRequest), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect for create product.", HttpStatus.OK, response.getStatusCode());
            ResourceCreated<Long> created = mapper.readValue(response.getBody(), new TypeReference<ResourceCreated<Long>>(){});
            return created.getId();
        } catch (IOException e) {
            fail(e.getMessage());
            return null;
        }
    }
}
