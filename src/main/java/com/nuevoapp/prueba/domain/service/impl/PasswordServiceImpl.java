package com.nuevoapp.prueba.domain.service.impl;

import com.nuevoapp.prueba.domain.model.dto.PasswordDto;
import com.nuevoapp.prueba.domain.model.entity.PasswordEntity;
import com.nuevoapp.prueba.domain.model.mappers.PasswordMapper;
import com.nuevoapp.prueba.domain.repository.PasswordRepository;
import com.nuevoapp.prueba.domain.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final PasswordMapper passwordMapper;
    @Override
    public String getUserPassword(UUID id){
        Optional<PasswordEntity> optPassword = passwordRepository.findById(id);
        return optPassword.map(PasswordEntity::getPassword).orElseThrow(() -> new NoSuchElementException("No elements found"));
    }

    @Override
    public void savePassword(PasswordDto dto){
        passwordRepository.save(passwordMapper.toEntity(dto));
    }

}
