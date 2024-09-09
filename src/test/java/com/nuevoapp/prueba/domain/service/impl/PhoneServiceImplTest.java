package com.nuevoapp.prueba.domain.service.impl;

import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.model.entity.PhoneEntity;
import com.nuevoapp.prueba.domain.model.mappers.PhoneMapper;
import com.nuevoapp.prueba.domain.repository.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PhoneServiceImplTest {

    @Mock
    private PhoneMapper phoneMapper;

    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    private UUID userId;
    private PhoneDto phoneDto;
    private PhoneEntity phoneEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        phoneDto = new PhoneDto();
        phoneDto.setNumber(1234567);
        phoneDto.setCityCode(1);
        phoneDto.setCountryCode("57");

        phoneEntity = new PhoneEntity();
        phoneEntity.setNumber(1234567);
        phoneEntity.setCityCode(1);
        phoneEntity.setCountryCode("57");
        phoneEntity.setUserId(userId);
    }

    @Test
    void testSaveAllPhones_HappyPath() {

        List<PhoneDto> phoneDtos = List.of(phoneDto);
        List<PhoneEntity> phoneEntities = List.of(phoneEntity);

        when(phoneMapper.toEntity(any(PhoneDto.class), eq(userId))).thenReturn(phoneEntity);
        when(phoneRepository.saveAll(anyList())).thenReturn(phoneEntities);
        when(phoneMapper.toDto(any(PhoneEntity.class))).thenReturn(phoneDto);

        List<PhoneDto> result = phoneService.saveAllPhones(phoneDtos, userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(phoneDto.getNumber(), result.get(0).getNumber());
        verify(phoneRepository).saveAll(anyList());
        verify(phoneMapper).toEntity(any(PhoneDto.class), eq(userId));
        verify(phoneMapper).toDto(any(PhoneEntity.class));
    }

    @Test
    void testGetAllUserPhones_HappyPath() {

        List<PhoneEntity> phoneEntities = List.of(phoneEntity);

        when(phoneRepository.findPhoneEntitiesByUserId(userId)).thenReturn(phoneEntities);
        when(phoneMapper.toDto(any(PhoneEntity.class))).thenReturn(phoneDto);

        List<PhoneDto> result = phoneService.getAllUserPhones(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(phoneDto.getNumber(), result.get(0).getNumber());
        verify(phoneRepository).findPhoneEntitiesByUserId(userId);
        verify(phoneMapper).toDto(any(PhoneEntity.class));
    }

    @Test
    void testAddUserPhone_HappyPath() {

        when(phoneMapper.toEntity(any(PhoneDto.class), eq(userId))).thenReturn(phoneEntity);
        when(phoneRepository.save(any(PhoneEntity.class))).thenReturn(phoneEntity);
        when(phoneMapper.toDto(any(PhoneEntity.class))).thenReturn(phoneDto);

        PhoneDto result = phoneService.addUserPhone(userId, phoneDto);

        assertNotNull(result);
        assertEquals(phoneDto.getNumber(), result.getNumber());
        verify(phoneMapper).toEntity(any(PhoneDto.class), eq(userId));
        verify(phoneRepository).save(any(PhoneEntity.class));
        verify(phoneMapper).toDto(any(PhoneEntity.class));
    }
}
