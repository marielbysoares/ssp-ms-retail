package com.tenx.ms.retail.product.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.retail.BaseRetailIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.product.rest.dto.Product;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class ProductControllerTest extends BaseRetailIntegrationTest {

    @Value("classpath:product/product_success.json")
    private File productSuccessRequest;

    @Value("classpath:product/product_success_response.json")
    private File productSuccessResponse;

    @Value("classpath:product/product_failed_invalid_sku.json")
    private File failedSkuRequest;

    @Value("classpath:product/product_failed_min_length_sku.json")
    private File failedMinLengthSkuRequest;

    @Value("classpath:product/product_failed_max_length_sku.json")
    private File failedMaxLengthSkuRequest;

    @Value("classpath:product/product_failed_no_sku.json")
    private File failedNoSkuRequest;

    @Value("classpath:product/product_failed_no_name.json")
    private File failedNoNameRequest;

    @Value("classpath:product/product_invalid_price.json")
    private File failedInvalidPriceRequest;

    @Value("classpath:product/product_no_price.json")
    private File failedNoPriceRequest;

    @Test
    @FlywayTest
    public void testCreateProductSuccess() {
        Long storeId = createStore();
        Long productId = createProduct(storeId);
        assertEquals("Product id does not match", Long.valueOf(1L), productId);
    }

    @Test
    @FlywayTest
    public void testCreateProductInvalidSku() {
        createProductExpectedError(failedSkuRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateProductNoSku() {
        createProductExpectedError(failedNoSkuRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateProductLengthSku() {
        createProductExpectedError(failedMinLengthSkuRequest, HttpStatus.PRECONDITION_FAILED);
        createProductExpectedError(failedMaxLengthSkuRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateProductNoName() {
        createProductExpectedError(failedNoSkuRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateProductInvalidPrice() {
        createProductExpectedError(failedInvalidPriceRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateProductNoPrice() {
        createProductExpectedError(failedNoPriceRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testGetStoreByIdSuccess() {
        Long storeId = createStore();
        Long productId = createProduct(storeId);
        ResponseEntity<String> response = getStringResponse(String.format(PRODUCTS_REQUEST_URI + "%s", getBasePath(), storeId, productId), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect for get product by id.", HttpStatus.OK, response.getStatusCode());

        try {
            String expectedJson = FileUtils.readFileToString(productSuccessResponse);
            JSONAssert.assertEquals(response.getBody(), expectedJson, false);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testGetStoreByIdNotFo() {
        Long storeId = createStore();
        Long productId = createProduct(storeId);
        ResponseEntity<String> response = getStringResponse(String.format(PRODUCTS_REQUEST_URI + "%s", getBasePath(), storeId, productId), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect for get product.", HttpStatus.OK, response.getStatusCode());

        try {
            String expectedJson = FileUtils.readFileToString(productSuccessResponse);
            JSONAssert.assertEquals(response.getBody(), expectedJson, false);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testFindAllByStoreId() {
        try {
            Long storeId = createStore();
            Long firstProductId = createProduct(storeId);
            Long secondProductId = createProduct(storeId);
            ResponseEntity<String> response = getStringResponse(String.format(PRODUCTS_REQUEST_URI, getBasePath(), storeId), null, HttpMethod.GET);
            List<Product> stores = mapper.readValue(response.getBody(), new TypeReference<List<Product>>() {});
            assertEquals("HTTP Status code incorrect.", HttpStatus.OK, response.getStatusCode());
            assertNotNull("Store's product list should not be null", stores);
            assertEquals("Number of product found does not match", stores.size(), 2);
            assertEquals("Product id does not match", stores.get(0).getProductId(), firstProductId);
            assertEquals("Product id does not match", stores.get(1).getProductId(), secondProductId);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testFindAllByStoreIdNotFound() {
        ResponseEntity<String> response = getStringResponse(String.format(PRODUCTS_REQUEST_URI, getBasePath(), INVALID_ID), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect.", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void createProductExpectedError(File file, HttpStatus expectedStatus) {
        Long storeId = createStore();
        String body = "";
        try {
            body = FileUtils.readFileToString(file);
        } catch (IOException e) {
            TestCase.fail(e.getMessage());
        }

        ResponseEntity<String> response = getStringResponse(String.format(PRODUCTS_REQUEST_URI, getBasePath(), storeId), body, HttpMethod.POST);
        assertEquals("HTTP Status code incorrect for create product.", expectedStatus, response.getStatusCode());
    }
}
