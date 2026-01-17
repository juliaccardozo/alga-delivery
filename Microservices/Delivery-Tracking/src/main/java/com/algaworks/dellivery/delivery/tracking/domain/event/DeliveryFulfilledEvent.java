package com.algaworks.dellivery.delivery.tracking.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class DeliveryFulfilledEvent {
    private final OffsetDateTime occuredAt;
    private final UUID deliveryId;

}
