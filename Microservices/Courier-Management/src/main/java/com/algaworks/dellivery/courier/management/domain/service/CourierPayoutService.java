package com.algaworks.dellivery.courier.management.domain.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CourierPayoutService {
    public BigDecimal calculate(Double distanceInKm) {
        return new BigDecimal("10").multiply(new BigDecimal(distanceInKm)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
