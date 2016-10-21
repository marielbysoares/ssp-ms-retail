package com.tenx.ms.retail.stock.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    private final static EntityConverter<Stock, StockEntity> CONVERTER =
        new EntityConverter<>(Stock.class, StockEntity.class);

    @Transactional
    public Stock upsert(Long storeId, Long productId, Stock stock) {
        Optional<ProductEntity> product = productRepository.findOneByStoreStoreIdAndProductId(storeId, productId);
        if (!product.isPresent()) {
            throw new NoSuchElementException("Product does not exists");
        }

        StockEntity stockEntity = CONVERTER.toT2(stock);
        stockEntity.setProductId(productId);
        stockEntity.setProduct(product.get());
        stockEntity.setStore(product.get().getStore());
        return CONVERTER.toT1(stockRepository.save(stockEntity));
    }

    public Stock findByProductId(Long productId) {
        Optional<StockEntity> stockEntity = stockRepository.findOneByProductId(productId);
        if (!stockEntity.isPresent()) {
            throw new NoSuchElementException("Stock does not exists");
        }

        return CONVERTER.toT1(stockEntity.get());
    }
}
