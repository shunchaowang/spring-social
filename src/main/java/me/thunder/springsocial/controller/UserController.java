package me.thunder.springsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import me.thunder.springsocial.exception.ResourceNotFoundException;
import me.thunder.springsocial.model.User;
import me.thunder.springsocial.repository.UserRepository;
import me.thunder.springsocial.security.CurrentUser;
import me.thunder.springsocial.security.UserPrincipal;

/**
 * The UserController class contains a protected API to get the details of the
 * currently authenticated user.
 */
@RestController
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
    return userRepository.findById(userPrincipal.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
  }
}