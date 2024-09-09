package com.nuevoapp.prueba.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nuevoapp.prueba.config.annotation.validators.PasswordValidator;
import com.nuevoapp.prueba.domain.model.dto.login.LoginRequest;
import com.nuevoapp.prueba.domain.model.dto.user.UserRequest;
import com.nuevoapp.prueba.domain.service.UsersService;
import com.nuevoapp.prueba.utils.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static com.nuevoapp.prueba.utils.MockMvcUtils.getJacksonMessageConverterInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    AuthController controller;
    @Mock
    UsersService usersService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    Authentication authentication;

    protected ObjectMapper mapper = new ObjectMapper();

    protected MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc =
                standaloneSetup(controller)
                        .setMessageConverters(getJacksonMessageConverterInstance())
                        .build();
    }

    @Test
    void testLoginHappyPath() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("email");
        request.setPassword("pass");

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(request) ;

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);


        mvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(usersService).loginUser(any(LoginRequest.class));
    }

}
