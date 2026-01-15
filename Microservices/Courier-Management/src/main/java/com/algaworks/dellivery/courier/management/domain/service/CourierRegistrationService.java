package com.algaworks.dellivery.courier.management.domain.service;

import com.algaworks.dellivery.courier.management.api.model.CourierInput;
import com.algaworks.dellivery.courier.management.domain.model.Courier;
import com.algaworks.dellivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {

    private final CourierRepository courierRepository;

    public Courier create(@Valid CourierInput courierInput) {
        Courier courier = Courier.brandNew(courierInput.getName(), courierInput.getPhoneNumber());
        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, @Valid CourierInput courierInput) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));
        courier.setName(courierInput.getName());
        courier.setPhone(courierInput.getPhoneNumber());
        return courierRepository.saveAndFlush(courier);
    }
}
