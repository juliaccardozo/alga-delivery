package com.algaworks.dellivery.delivery.tracking.domain.service;

import com.algaworks.dellivery.delivery.tracking.api.model.ContactPointInput;
import com.algaworks.dellivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.dellivery.delivery.tracking.api.model.ItemInput;
import com.algaworks.dellivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.dellivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.dellivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.dellivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryInput input) {

        Delivery draft = Delivery.draft();
        handlePreparation(input, draft);

        return deliveryRepository.save(draft);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput input) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

        delivery.removeItems();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryInput input, Delivery draft) {
        ContactPointInput senderInput = input.getSender();
        ContactPointInput recipientInput = input.getRecipient();

        ContactPoint sender = ContactPoint.builder()
                .phone(senderInput.getPhone())
                .name(senderInput.getName())
                .complement(senderInput.getComplement())
                .number(senderInput.getNumber())
                .zipCode(senderInput.getZipCode())
                .street(senderInput.getStreet())
                .build();
        ContactPoint recipient = ContactPoint.builder()
                .phone(recipientInput.getPhone())
                .name(recipientInput.getName())
                .complement(recipientInput.getComplement())
                .number(recipientInput.getNumber())
                .zipCode(recipientInput.getZipCode())
                .street(recipientInput.getStreet())
                .build();

        Duration expectedDeliveryTime = Duration.ofDays(3);
        BigDecimal payout = new BigDecimal("10.00");
        BigDecimal distanceFee = new BigDecimal("10.00");

        var preparationDetais = Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(distanceFee)
                .courierPayout(payout)
                .expectedDeliveryTime(expectedDeliveryTime)
                .build();

        draft.editPreparationDetails(preparationDetais);

        for (ItemInput itemInput : input.getItems()) {
            draft.addItem(itemInput.getName(), itemInput.getQuantity());
        }
    }
}
