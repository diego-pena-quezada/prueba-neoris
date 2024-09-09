package com.nuevoapp.prueba.domain.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.model.dto.UserDto;
import com.nuevoapp.prueba.domain.model.dto.login.LoginRequest;
import com.nuevoapp.prueba.domain.model.dto.login.LoginResponse;
import com.nuevoapp.prueba.domain.model.dto.user.UserRequest;
import com.nuevoapp.prueba.domain.model.dto.user.UserResponse;
import com.nuevoapp.prueba.domain.model.enums.UserRolesEnum;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    UserDto getUserByEmail(String email);

    ResponseEntity<LoginResponse> loginUser(LoginRequest request);

    UserResponse signUpUser(UserRequest userRequest);

    List<PhoneDto> getPhonesFromUser(UUID userId, String token);

    PhoneDto addUserPhone(UUID userId, String token, PhoneDto phone);

    List<PhoneDto> addUserPhoneBatch(UUID userId, String token, List<PhoneDto> phones);

    List<UserDto> getAllUsersByRole(UserRolesEnum role);

    UserDto patchUser(UUID userId, JsonPatch patchOperations);
}
