package com.nuevoapp.prueba.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatch;
import com.nuevoapp.prueba.domain.model.dto.PhoneDto;
import com.nuevoapp.prueba.domain.service.UsersService;
import com.nuevoapp.prueba.utils.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.nuevoapp.prueba.utils.MockMvcUtils.getJacksonMessageConverterInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController controller;
    @Mock
    UsersService service;

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
    void testGetAllPhones() throws Exception {
        mvc.perform(get("/user/{id}/phones", "a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3")
                        .header("Authorization", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).getPhonesFromUser(any(UUID.class), any(String.class));
    }

    @Test
    void testAddPhone() throws Exception {
        PhoneDto request = FileUtil.loadObject("json/PhoneRequest.json", PhoneDto.class);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(request) ;

        mvc.perform(post("/user/{id}/phones", "a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3")
                        .header("Authorization", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).addUserPhone(any(UUID.class), any(String.class), any(PhoneDto.class));
    }

    @Test
    void testAddPhoneBatch() throws Exception {
        PhoneDto request = FileUtil.loadObject("json/PhoneRequest.json", PhoneDto.class);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(List.of(request)) ;

        mvc.perform(post("/user/{id}/phones/batch", "a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3")
                        .header("Authorization", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).addUserPhoneBatch(any(UUID.class), any(String.class), anyList());
    }
}
