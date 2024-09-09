package com.nuevoapp.prueba.domain.service;

import com.nuevoapp.prueba.domain.model.dto.PasswordDto;

import java.util.UUID;

public interface PasswordService {
    String getUserPassword(UUID id);

    void savePassword(PasswordDto dto);
}
