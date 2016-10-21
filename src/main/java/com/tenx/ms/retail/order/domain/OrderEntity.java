package com.tenx.ms.retail.order.domain;

import com.tenx.ms.retail.order.rest.dto.OrderStatusEnum;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "purchase_order")
public class OrderEntity {
    @Id
    @GeneratedValue
    @Column(name="order_id", nullable = false)
    private Long orderId;

    @Column(name="status", nullable = false, length = 7)
    private OrderStatusEnum status;

    @Column(name = "order_date", columnDefinition = "datetime default current_timestamp",
            insertable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @ManyToOne(targetEntity = StoreEntity.class)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @OneToMany(targetEntity = OrderProductEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderProductEntity> products;
}
