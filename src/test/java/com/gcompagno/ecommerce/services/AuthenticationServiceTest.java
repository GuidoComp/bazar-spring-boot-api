package com.gcompagno.ecommerce.services;

import com.gcompagno.ecommerce.dtos.requestDTOs.AuthenticationRequest;
import com.gcompagno.ecommerce.dtos.requestDTOs.RegisterRequest;
import com.gcompagno.ecommerce.dtos.responseDTOs.AuthenticationResponse;
import com.gcompagno.ecommerce.models.Role;
import com.gcompagno.ecommerce.models.User;
import com.gcompagno.ecommerce.repositories.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private IUserRepository repository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register_withValidRequest_createsUserWithUserRoleAndReturnsToken() {
        RegisterRequest request = new RegisterRequest("Juan", "Perez", "juan@example.com", "secret12");
        when(passwordEncoder.encode("secret12")).thenReturn("hashed");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertEquals(Role.USER, saved.getRole());
        assertEquals("juan@example.com", saved.getEmail());
        assertEquals("Juan", saved.getFirstname());
        assertEquals("Perez", saved.getLastname());
        assertEquals("hashed", saved.getPassword());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        AuthenticationRequest request = new AuthenticationRequest("juan@example.com", "secret12");
        User user = User.builder()
                .email("juan@example.com")
                .password("hashed")
                .role(Role.USER)
                .build();
        when(repository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.login(request);

        assertEquals("jwt-token", response.getToken());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_withUnknownEmail_throwsUsernameNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest("ghost@example.com", "whatever");
        when(repository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.login(request));

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }
}
