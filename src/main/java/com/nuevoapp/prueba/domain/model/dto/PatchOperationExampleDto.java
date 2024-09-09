package com.nuevoapp.prueba.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PatchOperationExampleDto {
    @Schema(name = "op", example = "add")
    public String op;
    @Schema(name = "path", example = "/fieldName")
    public String path;
    @Schema(name = "value", example = "fieldValue")
    public String value;
}
