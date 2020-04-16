package me.thunder.springsocial.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.thunder.springsocial.payload.LoginRequest;
import me.thunder.springsocial.repository.UserRepository;
import me.thunder.springsocial.security.TokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
    return null;
  }
}