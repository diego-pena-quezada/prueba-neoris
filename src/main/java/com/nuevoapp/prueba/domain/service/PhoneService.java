package com.nuevoapp.prueba.domain.service;

import com.nuevoapp.prueba.domain.model.dto.PhoneDto;

import java.util.List;
import java.util.UUID;

public interface PhoneService {
    List<PhoneDto> saveAllPhones(List<PhoneDto> phones, UUID userId);

    List<PhoneDto> getAllUserPhones(UUID userId);

    PhoneDto addUserPhone(UUID userId, PhoneDto phoneDto);
}
