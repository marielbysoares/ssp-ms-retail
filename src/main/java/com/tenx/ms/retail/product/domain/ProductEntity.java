package com.tenx.ms.retail.product.domain;

import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue
    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "sku", nullable = false, length = 10)
    private String sku;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(targetEntity = StoreEntity.class)
    @JoinColumn(name = "store_id", referencedColumnName = "store_id", nullable = false)
    private StoreEntity store;
}
