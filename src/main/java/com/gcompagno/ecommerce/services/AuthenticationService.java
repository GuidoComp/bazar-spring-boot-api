package com.gcompagno.ecommerce.services;

import com.gcompagno.ecommerce.dtos.requestDTOs.AuthenticationRequest;
import com.gcompagno.ecommerce.dtos.requestDTOs.RegisterRequest;
import com.gcompagno.ecommerce.dtos.responseDTOs.AuthenticationResponse;
import com.gcompagno.ecommerce.models.Role;
import com.gcompagno.ecommerce.models.User;
import com.gcompagno.ecommerce.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private IUserRepository repository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);
        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return new AuthenticationResponse(jwtService.generateToken(user));
    }
}
