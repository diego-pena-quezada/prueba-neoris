package com.nuevoapp.prueba.config.annotation.constraints;

import com.nuevoapp.prueba.config.annotation.validators.NotNullOrBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * El elemento anotado no debe estar en blanco ni ser nulo
 */
@Documented
@Constraint(validatedBy = NotNullOrBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullOrBlankConstraint {
    String message() default "no debe ser nulo or vacío";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}