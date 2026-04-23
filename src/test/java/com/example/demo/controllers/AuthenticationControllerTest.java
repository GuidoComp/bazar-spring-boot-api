package com.example.demo.controllers;

import com.example.demo.dtos.requestDTOs.AuthenticationRequest;
import com.example.demo.dtos.requestDTOs.RegisterRequest;
import com.example.demo.dtos.responseDTOs.AuthenticationResponse;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_withValidBody_returns200AndToken() throws Exception {
        RegisterRequest request = new RegisterRequest("Juan", "Perez", "juan@example.com", "secret12");
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthenticationResponse("jwt-token"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(authenticationService).register(any(RegisterRequest.class));
    }

    @Test
    void register_withLegacyAdminFieldInBody_jacksonIgnoresItAndServiceReceivesCleanDto() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("firstname", "Evil");
        body.put("lastname", "Hacker");
        body.put("email", "evil@example.com");
        body.put("password", "secret12");
        body.put("admin", true);

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthenticationResponse("jwt-token"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        ArgumentCaptor<RegisterRequest> captor = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(authenticationService).register(captor.capture());
        RegisterRequest received = captor.getValue();
        assertEquals("evil@example.com", received.getEmail());
        assertEquals("Evil", received.getFirstname());
    }

    @Test
    void register_withBlankEmail_returns400() throws Exception {
        RegisterRequest invalid = new RegisterRequest("Juan", "Perez", "", "secret12");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void register_withShortPassword_returns400() throws Exception {
        RegisterRequest invalid = new RegisterRequest("Juan", "Perez", "juan@example.com", "short");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void register_withInvalidEmailFormat_returns400() throws Exception {
        RegisterRequest invalid = new RegisterRequest("Juan", "Perez", "not-an-email", "secret12");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void login_withValidBody_returns200AndToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("juan@example.com", "secret12");
        when(authenticationService.login(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse("jwt-token"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
