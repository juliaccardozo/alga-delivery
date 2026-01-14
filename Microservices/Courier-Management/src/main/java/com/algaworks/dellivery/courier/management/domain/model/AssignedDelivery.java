package com.algaworks.dellivery.courier.management.domain.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class AssignedDelivery {

    @EqualsAndHashCode.Include
    private UUID id;

    private OffsetDateTime assignedAt;

    static AssignedDelivery pending(UUID deliveryId) {
        var delivery = new AssignedDelivery();
        delivery.setId(deliveryId);
        delivery.setAssignedAt(OffsetDateTime.now());
        return delivery;
    }
}
