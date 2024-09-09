package com.nuevoapp.prueba.domain.service.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.nuevoapp.prueba.domain.model.dto.PasswordDto;
import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.model.dto.UserDto;
import com.nuevoapp.prueba.domain.model.dto.login.LoginRequest;
import com.nuevoapp.prueba.domain.model.dto.login.LoginResponse;
import com.nuevoapp.prueba.domain.model.dto.user.UserRequest;
import com.nuevoapp.prueba.domain.model.dto.user.UserResponse;
import com.nuevoapp.prueba.domain.model.entity.UserEntity;
import com.nuevoapp.prueba.domain.model.enums.UserRolesEnum;
import com.nuevoapp.prueba.domain.model.enums.UserStatusEnum;
import com.nuevoapp.prueba.domain.model.mappers.UserMapper;
import com.nuevoapp.prueba.domain.repository.UsersRepository;
import com.nuevoapp.prueba.domain.service.PasswordService;
import com.nuevoapp.prueba.domain.service.PhoneService;
import com.nuevoapp.prueba.security.JwtHandler;
import com.nuevoapp.prueba.utils.PatchUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private JwtHandler jwtHandler;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordService passwordService;
    @Mock
    private PhoneService phoneService;
    @Mock
    private PatchUtils patchUtils;

    @InjectMocks
    private UsersServiceImpl usersService;

    @Test
    void testGetUserByEmail_Success() {
        String userEmail = "test@example.com";
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        when(usersRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(userEntity));

        when(userMapper.toDto(userEntity))
                .thenReturn(userDto);

        UserDto result = usersService.getUserByEmail(userEmail);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String userEmail = "nonexistent@example.com";

        when(usersRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> usersService.getUserByEmail(userEmail));
    }

    @Test
    void testLoginUser_Success() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");

        doNothing().when(usersRepository).updateUserStatusAndLastLoginDateByEmail(
                any(), any(), anyString());

        when(jwtHandler.generateJwtToken(loginRequest.getEmail()))
                .thenReturn("mockedJwtToken");

        ResponseEntity<LoginResponse> result = usersService.loginUser(loginRequest);

        assertNotNull(result);
        assertEquals(UserStatusEnum.ACTIVE, result.getBody().getStatus());
        assertNotNull(result.getBody().getLoginDate());
        assertEquals("mockedJwtToken", result.getBody().getToken());
    }

    @Test
    void testSignUpUser_HappyPath() {

        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setEmail("test@example.com");
        validUserRequest.setName("John");
        validUserRequest.setPassword("Password123");
        validUserRequest.setPhones(List.of(new PhoneDto()));

        ZonedDateTime now = ZonedDateTime.now();
        UserEntity validUserEntity = new UserEntity();
        validUserEntity.setId(UUID.randomUUID());
        validUserEntity.setEmail(validUserRequest.getEmail());
        validUserEntity.setCreatedDate(now);
        validUserEntity.setModifiedDate(now);
        validUserEntity.setStatus(UserStatusEnum.PENDING_APPROVAL);

        when(usersRepository.existsByEmail(validUserRequest.getEmail())).thenReturn(false);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(validUserEntity);
        when(usersRepository.save(validUserEntity)).thenReturn(validUserEntity);

        UserResponse response = usersService.signUpUser(validUserRequest);

        assertNotNull(response);
        assertEquals(validUserEntity.getId(), response.getId());
        assertEquals(validUserEntity.getCreatedDate(), response.getCreated());
        assertEquals(validUserEntity.getModifiedDate(), response.getModified());
        assertEquals(validUserEntity.getStatus(), response.getStatus());

        verify(usersRepository).save(any(UserEntity.class));

        ArgumentCaptor<PasswordDto> passwordCaptor = ArgumentCaptor.forClass(PasswordDto.class);
        verify(passwordService).savePassword(passwordCaptor.capture());
        assertEquals(validUserEntity.getId(), passwordCaptor.getValue().getUserId());
        assertEquals(validUserRequest.getPassword(), passwordCaptor.getValue().getPassword());

        verify(phoneService).saveAllPhones(validUserRequest.getPhones(), validUserEntity.getId());
    }

    @Test
    void testSignUpUser_EmailAlreadyExists() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        when(usersRepository.existsByEmail(anyString())).thenReturn(true);

        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> usersService.signUpUser(request)
        );

        assertEquals("Email ya registrado", exception.getMessage());

        verify(passwordService, never()).savePassword(any(PasswordDto.class));
        verify(phoneService, never()).saveAllPhones(anyList(), any(UUID.class));
    }

    @Test
    void testGetPhonesFromUser_HappyPath() {
        String token = "Bearer token";
        UUID userId = UUID.randomUUID();
        String userEmail = "test@example.com";
        when(jwtHandler.removePrefixfromJwt(anyString())).thenReturn(token);
        when(jwtHandler.extractUsername(anyString())).thenReturn(userEmail);
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setStatus(UserStatusEnum.ACTIVE);
        when(usersRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);
        when(phoneService.getAllUserPhones(userId)).thenReturn(List.of(new PhoneDto()));

        List<PhoneDto> phones = usersService.getPhonesFromUser(userId, token);

        assertNotNull(phones);
        verify(phoneService).getAllUserPhones(userId);
    }

    @Test
    void testAddUserPhone_HappyPath() {
        PhoneDto phoneDto = new PhoneDto();
        String token = "Bearer token";
        UUID userId = UUID.randomUUID();
        String userEmail = "test@example.com";
        when(jwtHandler.removePrefixfromJwt(anyString())).thenReturn(token);
        when(jwtHandler.extractUsername(anyString())).thenReturn(userEmail);
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setStatus(UserStatusEnum.ACTIVE);
        when(usersRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);
        when(phoneService.addUserPhone(any(UUID.class), any(PhoneDto.class))).thenReturn(phoneDto);

        PhoneDto addedPhone = usersService.addUserPhone(userId, token, phoneDto);

        assertNotNull(addedPhone);
        verify(phoneService).addUserPhone(eq(userId), any(PhoneDto.class));
    }

    @Test
    void testAddUserPhoneBatch_HappyPath() {

        List<PhoneDto> phones = List.of(new PhoneDto());
        String token = "Bearer token";
        UUID userId = UUID.randomUUID();
        String userEmail = "test@example.com";
        when(jwtHandler.removePrefixfromJwt(anyString())).thenReturn(token);
        when(jwtHandler.extractUsername(anyString())).thenReturn(userEmail);
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setStatus(UserStatusEnum.ACTIVE);
        when(usersRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);
        when(phoneService.saveAllPhones(anyList(), eq(userId))).thenReturn(phones);

        List<PhoneDto> addedPhones = usersService.addUserPhoneBatch(userId, token, phones);

        assertEquals(phones, addedPhones);
        verify(phoneService).saveAllPhones(phones, userId);
    }

    @Test
    void testGetAllUsersByRole_HappyPath() {
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();
        UserRolesEnum role = UserRolesEnum.ROLE_USER;
        List<UserEntity> userEntities = List.of(userEntity);
        when(usersRepository.findAllByRole(any(UserRolesEnum.class))).thenReturn(userEntities);
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(userDto);

        // Act
        List<UserDto> users = usersService.getAllUsersByRole(role);

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        verify(usersRepository).findAllByRole(role);
    }

    @Test
    void testPatchUser_HappyPath() {
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();
        UUID userId = UUID.randomUUID();
        JsonPatch patchOperations =  new JsonPatch(List.of(new JsonPatchOperation[]{}));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(patchUtils.applyPatch(any(UserDto.class), any(JsonPatch.class))).thenReturn(userDto);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(userEntity);
        when(usersRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(userDto);

        // Act
        UserDto patchedUser = usersService.patchUser(userId, patchOperations);

        // Assert
        assertNotNull(patchedUser);
        verify(usersRepository).findById(userId);
        verify(usersRepository).save(any(UserEntity.class));
    }


}
