package com.tenx.ms.retail.store.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.retail.BaseRetailIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.store.rest.dto.Store;
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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class StoreControllerTest extends BaseRetailIntegrationTest {

    @Value("classpath:store/store_success.json")
    private File storeSuccessRequest;

    @Value("classpath:store/store_success_name.json")
    private File storeSuccessOtherNameRequest;

    @Value("classpath:store/store_failed.json")
    private File failedRequest;

    @Test
    @FlywayTest
    public void testCreateStoreSuccess() {
        Long storeId = createStore();
        assertEquals("StoreId does not match", Long.valueOf(1L), storeId);
    }

    @Test
    @FlywayTest
    public void testCreateStoreInvalidName() {
        try {
            ResponseEntity<String> response = getStringResponse(String.format(STORES_REQUEST_URI, getBasePath()), FileUtils.readFileToString(failedRequest), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect for create store.", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testGetStoreByIdSuccess() {
        Long storeId = createStore();
        ResponseEntity<String> response = getStringResponse(String.format(STORES_REQUEST_URI + "%s", getBasePath(), storeId), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect for get store by id.", HttpStatus.OK, response.getStatusCode());

        try {
            Store store = mapper.readValue(response.getBody(), Store.class);
            Store expectedStore = mapper.readValue(FileUtils.readFileToString(storeSuccessRequest), Store.class);
            assertEquals("Store id does not match", storeId, store.getStoreId());
            assertEquals("Store name does not match", expectedStore.getName(), store.getName());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testGetStoreNotFound() {
        ResponseEntity<String> response = getStringResponse(String.format(STORES_REQUEST_URI + "%s", getBasePath(), INVALID_ID), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect for get store by id with invalid id.", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testFindAll() {
        try {
            Long firstStoreId = createStore();
            Long secondStoreId = createStore();
            ResponseEntity<String> response = getStringResponse(String.format(STORES_REQUEST_URI, getBasePath()), null, HttpMethod.GET);
            List<Store> stores = mapper.readValue(response.getBody(), new TypeReference<List<Store>>() {});
            assertNotNull("Store list should not be null", stores);
            assertEquals("Number of stores found does not match", stores.size(), 2);
            assertEquals("Store Id does not match", stores.get(0).getStoreId(), firstStoreId);
            assertEquals("Store Id does not match", stores.get(1).getStoreId(), secondStoreId);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testFindAllByName() {
        try {
            Long firstStoreId = createStore();
            getStringResponse(String.format(STORES_REQUEST_URI, getBasePath()), FileUtils.readFileToString(storeSuccessOtherNameRequest), HttpMethod.POST);
            ResponseEntity<String> response = getStringResponse(String.format(STORES_REQUEST_URI, getBasePath()) +"?name=Test Store", null, HttpMethod.GET);
            List<Store> stores = mapper.readValue(response.getBody(), new TypeReference<List<Store>>() {});
            assertNotNull("Store list should not be null", stores);
            assertEquals("Number of stores found does not match", stores.size(), 1);
            assertEquals("Store Id does not match", stores.get(0).getStoreId(), firstStoreId);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
