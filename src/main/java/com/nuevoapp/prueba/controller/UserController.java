package com.nuevoapp.prueba.controller;

import com.nuevoapp.prueba.config.error.dto.CustomError;
import com.nuevoapp.prueba.domain.model.dto.PatchOperationExampleDto;
import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.Implementation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Secured({"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
@RestController
@RequestMapping(("/user"))
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UsersService userService;

    @Operation(
            summary = "Gets all phones from a user",
            description = "Gets all phones from a user",
            tags = {"Phone-management"},
            security = {@SecurityRequirement(name = "Authorization")},
            parameters = @Parameter(name = "id" , schema = @Schema(implementation = UUID.class)),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PhoneDto.class))
                            )),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No entity Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            ))
            })
    @GetMapping("/{id}/phones")
    public List<PhoneDto> getUserPhoneList(@PathVariable UUID id, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return userService.getPhonesFromUser(id, token);
    }

    @Operation(
            summary = "Adds a phone for a user",
            description = "Adds a phone for a user",
            tags = {"Phone-management"},
            security = {@SecurityRequirement(name = "Authorization")},
            parameters = @Parameter(name = "id" , schema = @Schema(implementation = UUID.class)),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PhoneDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No entity Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            ))
            })
    @PostMapping("/{id}/phones")
    public PhoneDto addUserPhone(@PathVariable UUID id, @RequestBody PhoneDto phone, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return userService.addUserPhone(id, token, phone);
    }

    @Operation(
            summary = "Adds multiple phones for a user",
            description = "Adds multiple phones for a user",
            tags = {"Phone-management"},
            security = {@SecurityRequirement(name = "Authorization")},
            parameters = @Parameter(name = "id" , schema = @Schema(implementation = UUID.class)),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PhoneDto.class))
                            )),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No entity Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomError.class)
                            ))
            })
    @PostMapping("/{id}/phones/batch")
    public List<PhoneDto> addUserPhoneBatch(@PathVariable UUID id, @RequestBody List<PhoneDto> phones, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return userService.addUserPhoneBatch(id, token, phones);
    }
}
