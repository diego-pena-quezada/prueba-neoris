package com.nuevoapp.prueba.config.annotation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nuevoapp.prueba.config.annotation.constraints.PasswordConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Value("${user.password.regex}")
    private String passwordRegex;

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches(passwordRegex);
    }
}