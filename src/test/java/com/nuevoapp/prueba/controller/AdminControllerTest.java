package com.nuevoapp.prueba.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.nuevoapp.prueba.domain.model.enums.UserRolesEnum;
import com.nuevoapp.prueba.domain.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static com.nuevoapp.prueba.utils.MockMvcUtils.getJacksonMessageConverterInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    AdminController controller;

    @Mock
    UsersService usersService;

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
    void testGetUserHappyPath() throws Exception {
        mvc.perform(get("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(usersService).getAllUsersByRole(any(UserRolesEnum.class));
    }

    @Test
    void testPatchUserHappyPath() throws Exception{
        JsonPatch patchOperations = new JsonPatch(List.of(new JsonPatchOperation[]{}));
        mvc.perform(patch("/admin/user/{id}", "a2c3e3d8-27b6-4a4e-b46e-cd59fc44e7d3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patchOperations)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(usersService).patchUser(any(UUID.class), any(JsonPatch.class));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
