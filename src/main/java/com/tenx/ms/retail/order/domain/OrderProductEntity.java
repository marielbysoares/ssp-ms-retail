package com.tenx.ms.retail.order.domain;

import com.tenx.ms.retail.product.domain.ProductEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "purchase_order_product")
public class OrderProductEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_product_id", nullable = false, insertable = false, updatable = false)
    private Long orderProductId;

    @Column(name = "count", nullable = false)
    private Integer count;

    @OneToOne(targetEntity = ProductEntity.class)
    @JoinColumn(name = "product_id",  referencedColumnName = "product_id", updatable = false)
    private ProductEntity product;

    @ManyToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", insertable = false, updatable = false)
    private OrderEntity order;
}
