package com.nuevoapp.prueba.domain.repository;

import com.nuevoapp.prueba.domain.model.entity.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordRepository extends JpaRepository<PasswordEntity, UUID>{
}
