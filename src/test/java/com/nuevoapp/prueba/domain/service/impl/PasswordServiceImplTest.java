package com.nuevoapp.prueba.domain.service.impl;

import com.nuevoapp.prueba.domain.model.dto.PasswordDto;
import com.nuevoapp.prueba.domain.model.entity.PasswordEntity;
import com.nuevoapp.prueba.domain.model.mappers.PasswordMapper;
import com.nuevoapp.prueba.domain.repository.PasswordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceImplTest {

    @InjectMocks
    private PasswordServiceImpl passwordService;
    @Mock
    private PasswordRepository passwordRepository;
    @Mock
    private PasswordMapper passwordMapper;



    @Test
    void testGetUserPassword_Success() {

        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setUserId(UUID.fromString("a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3"));
        passwordEntity.setPassword("hashedPassword");

        when(passwordRepository.findById(passwordEntity.getUserId()))
                .thenReturn(Optional.of(passwordEntity));

        String result = passwordService.getUserPassword(UUID.fromString("a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3"));

        Assertions.assertEquals("hashedPassword", result);
    }

    @Test
    void testGetUserPassword_UserNotFound() {
        String userEmail = "nonexistent@example.com";

        when(passwordRepository.findById(UUID.fromString("a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3")))
                .thenReturn(Optional.empty());

        // Perform the actual test and assert NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, () -> passwordService.getUserPassword(UUID.fromString("a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3")));
    }

    @Test
    void testSavePassword() {
        PasswordDto dto = new PasswordDto();
        PasswordEntity entity = new PasswordEntity();
        when(passwordMapper.toEntity(dto)).thenReturn(entity);
        when(passwordRepository.save(entity)).thenReturn(entity);
        passwordService.savePassword(dto);
        verify(passwordRepository).save(any(PasswordEntity.class));
    }
}
