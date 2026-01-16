package com.algaworks.dellivery.delivery.tracking.domain.service;

import java.math.BigDecimal;

public interface CourierPayoutCalculationService {
    BigDecimal calculatePayout(Double totalDistanceInKm);
}
