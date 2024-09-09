package com.nuevoapp.prueba.config.annotation.validators;

import com.nuevoapp.prueba.config.annotation.constraints.NotNullOrBlankConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotNullOrBlankValidator implements ConstraintValidator<NotNullOrBlankConstraint, String> {

    @Override
    public void initialize(NotNullOrBlankConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.trim().isEmpty();
    }
}