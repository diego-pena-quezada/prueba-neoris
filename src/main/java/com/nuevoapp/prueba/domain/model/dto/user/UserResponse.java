package com.nuevoapp.prueba.domain.model.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.nuevoapp.prueba.domain.model.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    @JsonAlias("last_login")
    private ZonedDateTime lastLogin;
    private UserStatusEnum status;
}
