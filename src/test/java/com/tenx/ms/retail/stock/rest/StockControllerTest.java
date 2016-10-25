package com.tenx.ms.retail.stock.rest;

import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class StockControllerTest extends BaseRetailIntegrationTest {
    @Value("classpath:stock/stock_success.json")
    private File stockSuccessRequest;

    @Value("classpath:stock/stock_failed.json")
    private File failedRequest;

    @Value("classpath:stock/stock_failed_no_count.json")
    private File failedNoCountRequest;

    @Test
    @FlywayTest
    public void testUpsertStockSuccess() {
        Long storeId = createStore();
        Long productId = createProduct(storeId);
        createStock(storeId, productId);
    }

    @Test
    @FlywayTest
    public void testUpsertStockProductNotFound() {
        Long storeId = createStore();
        String body = "";
        try {
            body = FileUtils.readFileToString(stockSuccessRequest);
        } catch (IOException e) {
            TestCase.fail(e.getMessage());
        }

        ResponseEntity<String> response = getStringResponse(String.format(STOCK_REQUEST_URI, getBasePath(), storeId, INVALID_ID), body, HttpMethod.POST);
        assertEquals("HTTP Status code incorrect for add/update stock.", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testUpsertStockInvalidCount() {
        createStockExpectedError(failedRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testUpsertStockNoCount() {
        createStockExpectedError(failedNoCountRequest, HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    @FlywayTest
    public void testGetStockByIdSuccess() {
        try {
            Long storeId = createStore();
            Long productId = createProduct(storeId);
            createStock(storeId, productId);
            Stock stock = findStock(storeId, productId);
            Stock expectedStock = mapper.readValue(FileUtils.readFileToString(stockSuccessRequest), new TypeReference<Stock>() {});
            assertEquals("Stock's count does not match", expectedStock.getCount(), stock.getCount());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void createStockExpectedError(File file, HttpStatus expectedStatus) {
        Long storeId = createStore();
        Long productId = createProduct(storeId);
        String body = "";
        try {
            body = FileUtils.readFileToString(file);
        } catch (IOException e) {
            TestCase.fail(e.getMessage());
        }

        ResponseEntity<String> response = getStringResponse(String.format(STOCK_REQUEST_URI, getBasePath(), storeId, productId), body, HttpMethod.POST);
        assertEquals("HTTP Status code incorrect for add/update stock.", expectedStatus, response.getStatusCode());
    }
}
