package com.tenx.ms.retail.order.domain;

import com.tenx.ms.retail.order.rest.dto.OrderStatusEnum;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "purchase_order")
public class OrderEntity {
    @Id
    @GeneratedValue
    @Column(name="order_id", nullable = false)
    private Long orderId;

    @Column(name="status", nullable = false)
    private OrderStatusEnum status;

    @Column(name = "order_date", insertable = false, nullable = false)
    private LocalDateTime orderDate;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderProductEntity> products;
}
