package com.cos.security.config.oauth;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.config.oauth.provider.FacebookUserInfo;
import com.cos.security.config.oauth.provider.GoogleUserInfo;
import com.cos.security.config.oauth.provider.OAuth2UserInfo;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Autowired
    public void  setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo;

        if(Objects.equals(userRequest.getClientRegistration().getRegistrationId(), "google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(Objects.equals(userRequest.getClientRegistration().getRegistrationId(), "facebook")){
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else {
            throw new IllegalArgumentException("oAuth2UserInfo is null");
        }

        User userEntity = userRepository.findByUsername(oAuth2UserInfo.getName());
        if(userEntity == null){
            userEntity = User.builder()  .username(oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId())
                                                            .password(bCryptPasswordEncoder.encode("설정비밀번호"))
                                                            .email(oAuth2UserInfo.getEmail())
                                                            .role("ROLE_USER")
                                                            .provider(oAuth2UserInfo.getProvider())
                                                            .providerId(oAuth2UserInfo.getProviderId())
                                                .build();
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
