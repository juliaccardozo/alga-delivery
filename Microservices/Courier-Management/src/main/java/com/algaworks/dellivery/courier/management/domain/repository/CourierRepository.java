package com.algaworks.dellivery.courier.management.domain.repository;

import com.algaworks.dellivery.courier.management.domain.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourierRepository extends JpaRepository<Courier, UUID> {
    Optional<Courier> findTop1ByOrderByLastFulfilledDeliveryAtAsc();

    Optional<Courier> findByPendingDeliveries_id(UUID deliveryId);
}
