package com.nuevoapp.prueba.domain.model.mappers;

import com.nuevoapp.prueba.domain.model.dto.PasswordDto;
import com.nuevoapp.prueba.domain.model.entity.PasswordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordMapper {

    @Mapping(target = "password", expression = "java(com.nuevoapp.prueba.utils.EncoderUtils.encode(dto.getPassword()))")
    PasswordEntity toEntity(PasswordDto dto);
}
