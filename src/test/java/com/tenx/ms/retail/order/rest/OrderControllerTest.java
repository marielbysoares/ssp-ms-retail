package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.retail.BaseRetailIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.stock.rest.dto.Stock;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class OrderControllerTest extends BaseRetailIntegrationTest {

    @Value("classpath:order/order_success.json")
    private File successRequest;

    @Value("classpath:order/order_success_response.json")
    private File successResponse;

    @Value("classpath:order/order_invalid_firstName.json")
    private File failedInvalidFirstNameRequest;

    @Value("classpath:order/order_no_firstName.json")
    private File failedNoFirstNameRequest;

    @Value("classpath:order/order_invalid_lastName.json")
    private File failedInvalidLastNameRequest;

    @Value("classpath:order/order_no_lastName.json")
    private File failedNoLastNameRequest;

    @Value("classpath:order/order_invalid_email.json")
    private File failedInvalidEmailRequest;

    @Value("classpath:order/order_no_email.json")
    private File failedNoEmailRequest;

    @Value("classpath:order/order_invalid_phone.json")
    private File failedInvalidPhoneRequest;

    @Value("classpath:order/order_no_phone.json")
    private File failedNoPhoneRequest;

    @Value("classpath:order/order_no_products.json")
    private File failedNoProductsRequest;

    @Value("classpath:order/order_products_invalid_count.json")
    private File failedProductsInvalidCountRequest;

    @Value("classpath:order/order_products_no_count.json")
    private File failedProductsNoCountRequest;

    @Test
    @FlywayTest
    public void testCreateOrderSuccess() {
        Long storeId = createStore();
        Long productId = createProduct(storeId);
        Long secondProductId = createProduct(storeId);
        createStock(storeId, productId);
        createStock(storeId, secondProductId);
        Stock productStockBefore = findStock(storeId, productId);
        Stock secondProductStockBefore = findStock(storeId, secondProductId);
        Integer discount = productStockBefore.getCount() - 1;

        try {
            ResponseEntity<String> response = getStringResponse(String.format(ORDERS_REQUEST_URI, getBasePath(), storeId), FileUtils.readFileToString(successRequest), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect.", HttpStatus.OK, response.getStatusCode());
            String expectedJson = FileUtils.readFileToString(successResponse);
            JSONAssert.assertEquals(response.getBody(), expectedJson, false);

            Stock productStock = findStock(storeId, productId);
            assertEquals("Stock's count is incorrect.", discount, productStock.getCount());
            Stock secondProductStock = findStock(storeId, secondProductId);
            assertEquals("Stock's count is incorrect.", secondProductStockBefore.getCount(), secondProductStock.getCount());
        } catch (IOException e) {
            TestCase.fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateOrderInvalidFirstName() {
        createOrderExpectedError(failedInvalidFirstNameRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderNoFirstName() {
        createOrderExpectedError(failedNoFirstNameRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderInvalidLastName() {
        createOrderExpectedError(failedInvalidLastNameRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderNoLastName() {
        createOrderExpectedError(failedNoLastNameRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderInvalidEmail() {
        createOrderExpectedError(failedInvalidEmailRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderNoEmail() {
        createOrderExpectedError(failedNoEmailRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderInvalidPhone() {
        createOrderExpectedError(failedInvalidPhoneRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderNoPhone() {
        createOrderExpectedError(failedNoPhoneRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderNoProducts() {
        createOrderExpectedError(failedNoProductsRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderProductsInvalidCount() {
        createOrderExpectedError(failedProductsInvalidCountRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderProductsNoCount() {
        createOrderExpectedError(failedProductsNoCountRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testCreateOrderStoreNotFound() {
        String body = "";
        try {
            body = FileUtils.readFileToString(successRequest);
        } catch (IOException e) {
            TestCase.fail(e.getMessage());
        }

        ResponseEntity<String> response = getStringResponse(String.format(ORDERS_REQUEST_URI, getBasePath(), INVALID_ID), body, HttpMethod.POST);
        assertEquals("HTTP Status code incorrect for create order.", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void createOrderExpectedError(File file, HttpStatus expectedStatus) {
        Long storeId = createStore();
        createProduct(storeId);
        String body = "";
        try {
            body = FileUtils.readFileToString(file);
        } catch (IOException e) {
            TestCase.fail(e.getMessage());
        }

        ResponseEntity<String> response = getStringResponse(String.format(ORDERS_REQUEST_URI, getBasePath(), storeId), body, HttpMethod.POST);
        assertEquals("HTTP Status code incorrect for create order.", expectedStatus, response.getStatusCode());
    }
}
