package com.nuevoapp.prueba.domain.service.impl;

import com.github.fge.jsonpatch.JsonPatch;
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
import com.nuevoapp.prueba.domain.service.UsersService;
import com.nuevoapp.prueba.security.JwtHandler;
import com.nuevoapp.prueba.utils.PatchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final JwtHandler jwtHandler;
    private final PasswordService passwordService;
    private final PhoneService phoneService;
    private final UserMapper userMapper;
    private final PatchUtils patchUtils;
    @Override
    public UserDto getUserByEmail(String email){

        Optional<UserEntity> optUser = usersRepository.findByEmail(email);
        //basically, if present, map the user, else return 204
        return optUser.map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Usuario Inválido"));
    }

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginRequest request){

        usersRepository.updateUserStatusAndLastLoginDateByEmail(UserStatusEnum.ACTIVE, ZonedDateTime.now(), request.getEmail());
        return ResponseEntity.ok(LoginResponse.builder()
                .loginDate(ZonedDateTime.now())
                .status(UserStatusEnum.ACTIVE)
                .token(jwtHandler.generateJwtToken(request.getEmail()))
                .build());
    }

    @Override
    public UserResponse signUpUser(UserRequest userRequest){
        if(usersRepository.existsByEmail(userRequest.getEmail())){
            throw new DataIntegrityViolationException("Email ya registrado");
        }
        ZonedDateTime now = ZonedDateTime.now();
        UserDto user = UserDto.builder().email(userRequest.getEmail())
                .name(userRequest.getName())
                .role(UserRolesEnum.ROLE_USER)
                .status(UserStatusEnum.PENDING_APPROVAL)
                .createdDate(now)
                .modifiedDate(now).build();

        UserEntity userEntity = usersRepository.save(userMapper.toEntity(user));
        passwordService.savePassword(PasswordDto.builder().userId(userEntity.getId()).password(userRequest.getPassword()).build());
        phoneService.saveAllPhones(userRequest.getPhones(), userEntity.getId());

        return UserResponse.builder().id(userEntity.getId())
                .created(userEntity.getCreatedDate())
                .modified(userEntity.getModifiedDate())
                .lastLogin(userEntity.getLastLoginDate())
                .status(userEntity.getStatus())
                .build();
    }

    @Override
    public List<PhoneDto> getPhonesFromUser(UUID userId, String token){
        validateUserToken(userId, token);

        return phoneService.getAllUserPhones(userId);
    }

    @Override
    public PhoneDto addUserPhone(UUID userId, String token, PhoneDto phone){
        validateUserToken(userId, token);
        return phoneService.addUserPhone(userId, phone);
    }

    private void validateUserToken(UUID userId, String token) {
        token = jwtHandler.removePrefixfromJwt(token);
        String email = jwtHandler.extractUsername(token);
        UserDto user = this.getUserByEmail(email);

        if(!userId.equals(user.getId())){
            throw new SecurityException("Discrepancia de datos detectada");
        }
        if(!user.getStatus().equals(UserStatusEnum.ACTIVE)){
            throw new SecurityException("Usuario no tiene permitido realizar ésta acción");
        }
    }

    @Override
    public List<PhoneDto> addUserPhoneBatch(UUID userId, String token, List<PhoneDto> phones){
        validateUserToken(userId, token);
        return phoneService.saveAllPhones(phones, userId);
    }

    @Override
    public List<UserDto> getAllUsersByRole(UserRolesEnum role){
        return usersRepository.findAllByRole(role).stream().map(userMapper::toDto).toList();
    }

    //Este endpoint usa una implementación custom de la librería JSONPatch, para hacer uso del estandar RFC 6902 en las
    //solicitudes PATCH
    @Override
    public UserDto patchUser(UUID userId, JsonPatch patchOperations){
        Optional<UserEntity> optUser = usersRepository.findById(userId);
        if(optUser.isEmpty()){
            throw new NoSuchElementException("Usuario inválido");
        }
        UserDto user = userMapper.toDto(optUser.get());
        user = patchUtils.applyPatch(user, patchOperations);
        user.setModifiedDate(ZonedDateTime.now());

        UserEntity patchedEntity = userMapper.toEntity(user);
        return userMapper.toDto(usersRepository.save(patchedEntity));
    }
}
