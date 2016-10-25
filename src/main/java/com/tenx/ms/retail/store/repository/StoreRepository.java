package com.tenx.ms.retail.store.repository;


import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findOneByStoreId(Long storeId);

    Page<StoreEntity> findAllByName(Pageable pageable, String name);
}
