package com.gcompagno.ecommerce.controllers;

import com.gcompagno.ecommerce.dtos.requestDTOs.AuthenticationRequest;
import com.gcompagno.ecommerce.dtos.requestDTOs.RegisterRequest;
import com.gcompagno.ecommerce.dtos.responseDTOs.AuthenticationResponse;
import com.gcompagno.ecommerce.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Registro y login de usuarios. Devuelve un JWT que se envía como Bearer en el resto de los endpoints.")
public class AuthenticationController {
    @Autowired
    private AuthenticationService service;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea el usuario con rol USER y devuelve un JWT listo para usar. El campo 'admin' del body, si se manda, se ignora."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario creado, token devuelto"),
            @ApiResponse(responseCode = "400", description = "Body inválido (email mal formado, password < 8 chars, etc.)")
    })
    @SecurityRequirements
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(
            summary = "Login",
            description = "Verifica credenciales y devuelve un JWT firmado con HS256."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Credenciales válidas, token devuelto"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
