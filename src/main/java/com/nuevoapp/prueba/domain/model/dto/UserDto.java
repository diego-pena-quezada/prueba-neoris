package com.nuevoapp.prueba.domain.model.dto;

import com.nuevoapp.prueba.domain.model.enums.UserRolesEnum;
import com.nuevoapp.prueba.domain.model.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private UserRolesEnum role;
    private String name;
    private UserStatusEnum status;
    private ZonedDateTime lastLoginDate;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;

}
