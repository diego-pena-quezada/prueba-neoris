package com.nuevoapp.prueba.domain.model.dto.user;

import com.nuevoapp.prueba.config.annotation.constraints.NotNullOrBlankConstraint;
import com.nuevoapp.prueba.config.annotation.constraints.PasswordConstraint;
import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserRequest {
    @NotNullOrBlankConstraint
    private String name;
    @Email
    private String email;
    @PasswordConstraint(message = " debe tener al menos 8 caracteres y contener al menos una letra mayúscula, una letra minúscula y un dígito")
    private String password;
    private List<PhoneDto> phones;
}
