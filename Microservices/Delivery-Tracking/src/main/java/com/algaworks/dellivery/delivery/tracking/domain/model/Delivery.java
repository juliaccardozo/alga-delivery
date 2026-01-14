package com.algaworks.dellivery.delivery.tracking.domain.model;

import com.algaworks.dellivery.delivery.tracking.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Delivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        var delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity, this);
        this.items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void removeItems() {
        this.items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails details) {
        verifyIfCanBeEdited();
        this.setSender(details.getSender());
        this.setRecipient(details.getRecipient());
        this.setDistanceFee(details.getDistanceFee());
        this.setCourierPayout(details.getCourierPayout());

        this.setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        this.setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    public void place() {
        verifyIfCanBePlaced();
        changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId, BigDecimal courierPayout, OffsetDateTime expectedDeliveryAt) {
        changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setCourierId(courierId);
        this.setAssignedAt(OffsetDateTime.now());
    }

    public void markAsDelivered() {
        changeStatusTo(DeliveryStatus.DELIVERED);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    private void calculateTotalItems() {
        this.totalItems = getItems().stream().mapToInt(Item::getQuantity).sum();
    }

    public void changeItemQuantity(UUID itemId, Integer newQuantity) {
        Item item = getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow();

        if (newQuantity == null || newQuantity <= 0) {
            this.items.removeIf(i -> i.getId().equals(itemId));
        } else {
            item.setQuantity(newQuantity);
        }
        calculateTotalItems();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void changeStatusTo(DeliveryStatus newStatus) {
        if (newStatus != null && this.getStatus().canNotChangeTo(newStatus)) {
            throw new DomainException(
                    String.format("Cannot change delivery status from %s to %s", this.getStatus(), newStatus)
            );
        }
        this.setStatus(newStatus);
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new DomainException("Delivery is not filled properly to be placed.");
        }
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException("Only deliveries in DRAFT status can be placed.");
        }
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException("Only deliveries in DRAFT status can be edited.");
        }
    }

    private boolean isFilled() {
        return getSender() != null
                && getRecipient() != null
                && this.getTotalCost() != null;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }
}
