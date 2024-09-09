package com.nuevoapp.prueba.security;

import com.nuevoapp.prueba.domain.model.dto.UserDto;
import com.nuevoapp.prueba.domain.service.PasswordService;
import com.nuevoapp.prueba.domain.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsersService usersService;
    private final PasswordService passwordService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDto user = usersService.getUserByEmail(email);
        String password = passwordService.getUserPassword(user.getId());

        return new User(
                user.getEmail(), password, true, true, true,
                true, List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }


}
