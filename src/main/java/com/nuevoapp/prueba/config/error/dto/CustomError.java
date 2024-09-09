package com.nuevoapp.prueba.config.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomError {

    private HttpStatus status;
    private int code;
    private String timestamp;
    private String message;
    private List<String> errors;
}
