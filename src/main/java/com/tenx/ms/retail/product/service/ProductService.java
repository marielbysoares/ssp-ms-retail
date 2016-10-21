package com.tenx.ms.retail.product.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final static EntityConverter<Product, ProductEntity> CONVERTER =
        new EntityConverter<>(Product.class, ProductEntity.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public Long create(Long storeId, Product product) {
        Optional<StoreEntity> storeEntity = storeRepository.findOneByStoreId(storeId);
        ProductEntity productEntity = CONVERTER.toT2(product);

        if (!storeEntity.isPresent()) {
           throw new NoSuchElementException("Product's store does not exists");
        }

        productEntity.setStore(storeEntity.get());
        return productRepository.save(productEntity).getProductId();
    }

    public List findAllByStoreId(Long storeId) {
        Optional<StoreEntity> storeEntity = storeRepository.findOneByStoreId(storeId);
        if (!storeEntity.isPresent()) {
            throw new NoSuchElementException("Store does not exists");
        }

        List<ProductEntity> productEntities = productRepository.findAllByStoreStoreId(storeId);
        return productEntities.stream().map(CONVERTER::toT1).collect(Collectors.toList());
    }

    public Product getByStoreIdAndProductId(Long storeId, Long productId) {
        Optional<Product> product = productRepository.findOneByStoreStoreIdAndProductId(storeId, productId).map(CONVERTER::toT1);
        if (!product.isPresent()) {
            throw new NoSuchElementException("Product does not exists");
        }
        return product.get();
    }
}
