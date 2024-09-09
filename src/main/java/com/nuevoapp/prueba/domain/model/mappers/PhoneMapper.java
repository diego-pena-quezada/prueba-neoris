package com.nuevoapp.prueba.domain.model.mappers;

import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.model.entity.PhoneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneDto toDto(PhoneEntity entity);
    PhoneEntity toEntity(PhoneDto dto, UUID userId);
}
