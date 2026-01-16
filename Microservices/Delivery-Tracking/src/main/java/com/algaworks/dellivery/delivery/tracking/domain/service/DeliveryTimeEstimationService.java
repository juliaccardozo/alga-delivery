package com.algaworks.dellivery.delivery.tracking.domain.service;

import com.algaworks.dellivery.delivery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {
    DeliveryEstimate estimate(ContactPoint sender, ContactPoint recipient);
}
