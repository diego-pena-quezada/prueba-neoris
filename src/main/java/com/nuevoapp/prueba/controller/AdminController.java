package com.nuevoapp.prueba.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.nuevoapp.prueba.config.error.dto.CustomError;
import com.nuevoapp.prueba.domain.model.dto.PatchOperationExampleDto;
import com.nuevoapp.prueba.domain.model.dto.UserDto;
import com.nuevoapp.prueba.domain.model.dto.login.LoginResponse;
import com.nuevoapp.prueba.domain.model.enums.UserRolesEnum;
import com.nuevoapp.prueba.domain.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador para usuarios con role ROLE_ADMIN, cualquier otro rol retorna un AccessDeniedException
 */

@RestController
@RequestMapping(("/admin"))
@RequiredArgsConstructor
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminController {

    private final UsersService usersService;
    @Operation(
            summary = "Get all users with role USER",
            description = "Get all users with role USER",
            security = {@SecurityRequirement(name = "Authorization")},
            tags = {"Admin"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Empty Response"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Invalid credentials",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            ))
            })
    @GetMapping("/users")
    public List<UserDto> getAllUsers(){
        return usersService.getAllUsersByRole(UserRolesEnum.ROLE_USER);
    }

    @Operation(
            summary = "Updates a User, must specify the action",
            description = "Updates a Task, must specify the action (“add”, “remove”, “replace”, “move”, “copy” and “test”)",
            tags = {"Admin"},
            security = {@SecurityRequirement(name = "Authorization")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = PatchOperationExampleDto.class))
                    )),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Entity updated",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No entity Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            ))
            })
    @PatchMapping("/user/{id}")
    public UserDto updateUser(@RequestBody JsonPatch jsonOperations, @PathVariable UUID id){
        return usersService.patchUser(id, jsonOperations);
    }
}
