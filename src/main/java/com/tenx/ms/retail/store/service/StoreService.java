package com.tenx.ms.retail.store.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final static EntityConverter<Store, StoreEntity> CONVERTER =
            new EntityConverter<>(Store.class, StoreEntity.class);

    @Autowired
    private StoreRepository storeRepository;

    public Store getById(Long storeId) {
        Optional<Store> store = storeRepository.findOneByStoreId(storeId).map(CONVERTER::toT1);
        if (!store.isPresent()) {
            throw new NoSuchElementException("Store does not exists");
        }
        return store.get();
    }

    public Long create(Store store) {
        StoreEntity storeEntity = storeRepository.save(CONVERTER.toT2(store));
        return storeEntity.getStoreId();
    }

    public List<Store> findAll(Pageable pageable) {
        List<StoreEntity> storeEntities = storeRepository.findAll(pageable).getContent();
        return storeEntities.stream().map(CONVERTER::toT1).collect(Collectors.toList());
    }

    public List<Store> findAllByName(Pageable pageable, String name) {
        List<StoreEntity> storeEntities = storeRepository.findAllByName(pageable, name).getContent();
        return storeEntities.stream().map(CONVERTER::toT1).collect(Collectors.toList());
    }

    public void delete(Long storeId) {
        Optional<Store> store = storeRepository.findOneByStoreId(storeId).map(CONVERTER::toT1);
        if (!store.isPresent()) {
            throw new NoSuchElementException("Store does not exists");
        }
        storeRepository.delete(storeId);
    }
}
