package com.tenx.ms.retail.stock.domain;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "stock")
public class StockEntity {
    @Id
    @Column(name = "product_id", insertable = false, updatable = false, nullable = false)
    private Long productId;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private StoreEntity store;

    @Column(name = "count", nullable = false)
    private Integer count;
}
