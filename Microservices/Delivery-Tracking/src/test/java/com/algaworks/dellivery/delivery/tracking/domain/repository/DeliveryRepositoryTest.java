package com.algaworks.dellivery.delivery.tracking.domain.repository;

import com.algaworks.dellivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.dellivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist() {
        var delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.addItem("Computador", 5);
        delivery.addItem("Monitor", 5);

        deliveryRepository.saveAndFlush(delivery);
        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2, persistedDelivery.getItems().size());
        assertNotNull(persistedDelivery.getItems().get(0).getId());
        assertNotNull(persistedDelivery.getItems().get(1).getId());
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