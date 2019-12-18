package me.thunder.springsocial.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository {

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
      HttpServletResponse response) {
    // TODO Auto-generated method stub

  }

}