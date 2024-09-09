package com.nuevoapp.prueba.domain.model.dto.login;

import com.nuevoapp.prueba.domain.model.enums.UserStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private UserStatusEnum status;
    private ZonedDateTime loginDate;
}
