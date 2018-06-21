package com.piotrkalitka.noteman.controller;

import com.piotrkalitka.noteman.ResponseMessages;
import com.piotrkalitka.noteman.model.User;
import com.piotrkalitka.noteman.payload.ApiResponse;
import com.piotrkalitka.noteman.payload.JwtAuthenticationResponse;
import com.piotrkalitka.noteman.payload.LoginRequest;
import com.piotrkalitka.noteman.payload.RegisterRequest;
import com.piotrkalitka.noteman.repository.UserRepository;
import com.piotrkalitka.noteman.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping(value = "/api/auth")
@RestController
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest model) {
        if (userRepository.existsByEmail(model.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, ResponseMessages.REGISTER_EMAIL_TAKEN), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(model.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, ResponseMessages.REGISTER_USERNAME_TAKEN), HttpStatus.BAD_REQUEST);
        }
        if (!model.getPassword().equals(model.getPassword_confirmation())) {
            return new ResponseEntity<>(new ApiResponse(false, ResponseMessages.REGISTER_PASSWORDS_NOT_MATCHES), HttpStatus.BAD_REQUEST);
        }

        User user = new User(model.getEmail(), model.getName(), model.getSurname(), model.getUsername(), model.getPassword());
        user.setPassword(passwordEncoder.encode(model.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok().body(new ApiResponse(true, ResponseMessages.REGISTER_SUCCESSFULLY));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest model) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getLogin(), model.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}