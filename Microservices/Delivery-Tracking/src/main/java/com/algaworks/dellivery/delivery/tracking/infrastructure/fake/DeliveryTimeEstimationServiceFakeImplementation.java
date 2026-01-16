package com.algaworks.dellivery.delivery.tracking.infrastructure.fake;

import com.algaworks.dellivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.dellivery.delivery.tracking.domain.service.DeliveryEstimate;
import com.algaworks.dellivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;

public class DeliveryTimeEstimationServiceFakeImplementation implements DeliveryTimeEstimationService {
    @Override
    public DeliveryEstimate estimate(ContactPoint sender, ContactPoint recipient) {
        return new DeliveryEstimate(java.time.Duration.ofHours(3), 10.0);
    }
}
