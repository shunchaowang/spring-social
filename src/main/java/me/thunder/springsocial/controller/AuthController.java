package me.thunder.springsocial.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.thunder.springsocial.exception.BadRequestException;
import me.thunder.springsocial.model.AuthProvider;
import me.thunder.springsocial.model.User;
import me.thunder.springsocial.payload.ApiResponse;
import me.thunder.springsocial.payload.AuthResponse;
import me.thunder.springsocial.payload.LoginRequest;
import me.thunder.springsocial.payload.SignUpRequest;
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
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = tokenProvider.createToken(authentication);
    return ResponseEntity.ok(new AuthResponse(token));
  }

  public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new BadRequestException("Email " + signUpRequest.getEmail() + " already in use.");
    }

    // Create user's account
    User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(signUpRequest.getPassword());
    user.setName(signUpRequest.getEmail());
    user.setProvider(AuthProvider.local);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    User result = userRepository.save(user);

    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me").buildAndExpand(result.getId())
        .toUri();
    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "User " + result.getName() + " registered successfully@"));
  }
}