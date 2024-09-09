package com.nuevoapp.prueba.domain.repository;

import com.nuevoapp.prueba.domain.model.entity.UserEntity;
import com.nuevoapp.prueba.domain.model.enums.UserRolesEnum;
import com.nuevoapp.prueba.domain.model.enums.UserStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity SET status = :status, lastLoginDate = :now where email = :email")
    void updateUserStatusAndLastLoginDateByEmail(UserStatusEnum status, ZonedDateTime now, String email);

    List<UserEntity> findAllByRole(UserRolesEnum role);
}
