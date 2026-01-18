package com.algaworks.dellivery.delivery.tracking.infrastructure.http.client;

import com.algaworks.dellivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceImpl implements CourierPayoutCalculationService {

    private final CourierAPIClient courierAPIClient;

    @Override
    public java.math.BigDecimal calculatePayout(Double totalDistanceInKm) {
        try {
            var courierPayoutResultModel = courierAPIClient.payoutCalculation(
                    new CourierPayoutCalculationInput(totalDistanceInKm)
            );
            return courierPayoutResultModel.getPayoutFee();
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException(e);
        } catch (HttpServerErrorException | IllegalArgumentException | CallNotPermittedException e) {
            throw new BadGatewayException(e);
        }
    }
}
