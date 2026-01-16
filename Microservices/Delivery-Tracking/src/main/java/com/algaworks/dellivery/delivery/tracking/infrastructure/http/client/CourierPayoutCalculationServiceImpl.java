package com.algaworks.dellivery.delivery.tracking.infrastructure.http.client;

import com.algaworks.dellivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceImpl implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public java.math.BigDecimal calculatePayout(Double totalDistanceInKm) {
        var courierPayoutResultModel = courierAPIClient.payoutCalculation(
                new CourierPayoutCalculationInput(totalDistanceInKm)
        );
        return courierPayoutResultModel.getPayoutFee();
    }
}
