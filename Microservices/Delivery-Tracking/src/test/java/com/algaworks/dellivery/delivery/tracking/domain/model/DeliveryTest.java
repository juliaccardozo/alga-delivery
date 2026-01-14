package com.algaworks.dellivery.delivery.tracking.domain.model;

import com.algaworks.dellivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shouldPlace() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    @Test
    public void shouldNotPlace() {
        Delivery delivery = Delivery.draft();

        assertThrows(DomainException.class, delivery::place);

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        ContactPoint sender = ContactPoint.builder()
                .zipCode("12345-678")
                .street("Sender Street")
                .number("100")
                .complement("Apt 1")
                .name("John Doe")
                .phone("(11) 12345-6789")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode("98765-432")
                .street("Sender Avenue")
                .number("10")
                .complement("")
                .name("Jane Doe")
                .phone("(50) 12345-6789")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("10.00"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }

}