package com.tenx.ms.retail.order.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.domain.OrderProductEntity;
import com.tenx.ms.retail.order.repository.OrderRepository;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderProduct;
import com.tenx.ms.retail.order.rest.dto.OrderResponse;
import com.tenx.ms.retail.order.rest.dto.OrderStatusEnum;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    private final static EntityConverter<Order, OrderEntity> CONVERTER =
            new EntityConverter<>(Order.class, OrderEntity.class);

    private final static EntityConverter<OrderProduct, OrderProductEntity> ITEMCONVERTER =
            new EntityConverter<>(OrderProduct.class, OrderProductEntity.class);

    @Transactional
    public OrderResponse create(Long storeId, Order order) {
        Optional<StoreEntity> store = storeRepository.findOneByStoreId(storeId);
        if (!store.isPresent()) {
            throw new NoSuchElementException("Store does not exists");
        }

        OrderEntity orderEntity = CONVERTER.toT2(order);
        orderEntity.setStatus(OrderStatusEnum.ORDERED);
        orderEntity.setStore(store.get());

        List<OrderProductEntity> items = new ArrayList<>();
        List<OrderProduct> backorderedItems = new ArrayList<>();

        for (OrderProduct orderProduct: order.getProducts()) {
            Optional<ProductEntity> productEntity =
                    productRepository.findOneByStoreStoreIdAndProductId(storeId, orderProduct.getProductId());

            if (!productEntity.isPresent()) {
                throw new NoSuchElementException(String.format("Product (%d) does not exists", orderProduct.getProductId()));
            }

            ProductEntity product = productEntity.get();
            StockEntity stockEntity = product.getStock();
            if (stockEntity != null && stockEntity.getCount() >= orderProduct.getCount()) {
                stockEntity.setCount(stockEntity.getCount() - orderProduct.getCount());
                stockRepository.save(stockEntity);

                OrderProductEntity item = ITEMCONVERTER.toT2(orderProduct);
                item.setProduct(product);
                items.add(item);
            } else {
                backorderedItems.add(orderProduct);
            }
        }

        orderEntity.setProducts(items);
        OrderEntity orderResponse = orderRepository.save(orderEntity);
        return new OrderResponse(orderResponse.getOrderId(), OrderStatusEnum.ORDERED, backorderedItems);
    }
}
