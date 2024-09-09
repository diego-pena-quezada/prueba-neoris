package com.nuevoapp.prueba.domain.repository;

import com.nuevoapp.prueba.domain.model.entity.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhoneRepository  extends JpaRepository<PhoneEntity, UUID> {
    List<PhoneEntity> findPhoneEntitiesByUserId(UUID userId);
}
