package me.thunder.springsocial.security.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import me.thunder.springsocial.exception.OAuth2AuthenticationProcessingException;
import me.thunder.springsocial.model.AuthProvider;
import me.thunder.springsocial.model.User;
import me.thunder.springsocial.repository.UserRepository;
import me.thunder.springsocial.security.UserPrincipal;
import me.thunder.springsocial.security.oauth2.user.OAuth2UserInfo;
import me.thunder.springsocial.security.oauth2.user.OAuth2UserInfoFactory;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException exception) {
      throw exception;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException will trigger the
      // OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
        .getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getClientId(), oAuth2User.getAttributes());
    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider.");
    }

    Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
    User user;
    if (userOptional.isPresent()) {
      user = userOptional.get();

      // check if user is authenticating with the right provider
      if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getClientId()))) {
        throw new OAuth2AuthenticationProcessingException("You've signed us with " + user.getProvider()
            + " account. Please use your " + user.getProvider() + " account to login.");
      }
      user = updateExistingUser(user, oAuth2UserInfo);
    } else {
      user = registerUser(oAuth2UserRequest, oAuth2UserInfo);
    }
    return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
  }

  private User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user = new User();
    user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getClientId()));
    user.setProviderId(oAuth2UserInfo.getId());
    user.setName(oAuth2UserInfo.getName());
    user.setEmail(oAuth2UserInfo.getEmail());
    user.setImageUrl(oAuth2UserInfo.getImageUrl());
    return userRepository.save(user);
  }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setName(oAuth2UserInfo.getName());
    existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
    return userRepository.save(existingUser);
  }
}