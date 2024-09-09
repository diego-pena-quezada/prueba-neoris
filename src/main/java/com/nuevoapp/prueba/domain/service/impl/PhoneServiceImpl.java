package com.nuevoapp.prueba.domain.service.impl;

import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.model.entity.PhoneEntity;
import com.nuevoapp.prueba.domain.model.mappers.PhoneMapper;
import com.nuevoapp.prueba.domain.repository.PhoneRepository;
import com.nuevoapp.prueba.domain.service.PhoneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PhoneServiceImpl implements PhoneService {
    private final PhoneMapper phoneMapper;
    private final PhoneRepository phoneRepository;

    @Override
    public List<PhoneDto> saveAllPhones(List<PhoneDto> phones , UUID userId){
        List<PhoneEntity> entities = phoneRepository.saveAll(phones.stream().map(phoneDto -> phoneMapper.toEntity(phoneDto,userId)).toList());
        return entities.stream().map(phoneMapper::toDto).toList();
    }

    @Override
    public List<PhoneDto> getAllUserPhones(UUID userId){
        return phoneRepository.findPhoneEntitiesByUserId(userId).stream().map(phoneMapper::toDto).toList();
    }

    @Override
    public PhoneDto addUserPhone(UUID userId, PhoneDto phoneDto){
        return phoneMapper.toDto(phoneRepository.save(phoneMapper.toEntity(phoneDto, userId)));
    }

}
