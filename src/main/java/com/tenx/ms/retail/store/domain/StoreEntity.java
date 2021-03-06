package com.tenx.ms.retail.store.domain;

import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.product.domain.ProductEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "store")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<ProductEntity> products;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<OrderEntity> orders;
}
