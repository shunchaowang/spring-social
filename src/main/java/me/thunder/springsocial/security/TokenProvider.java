package me.thunder.springsocial.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import me.thunder.springsocial.config.AppProperties;

@Service
public class TokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
  private final AppProperties appProperties;

  public TokenProvider(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  public String createToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    return null;
  }
}