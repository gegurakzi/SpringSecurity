package com.cos.security.config;

import com.cos.security.config.oauth.PrincipalOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
// @Secured 어노테이션을 활성화 한 도메인에 접근을 제한해줌
// @PreAuthorize와 @PostAuthorize 어노테이션을 활성화 한 도메인
// pre: 컨트롤러 메서드 실행 "전" 권한 확인
// post: 컨트롤러 메서드 실행 "후" 권한 확인
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PrincipalOAuth2UserService principalOAuth2UserService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setPrincipalOAuth2UserService(PrincipalOAuth2UserService principalOAuth2UserService){
        this.principalOAuth2UserService = principalOAuth2UserService;
    }
    @Autowired
    public void  setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                //도메인에 따른 권한 제한 및 배정
                .antMatchers("/user/**").authenticated()
                // DB에 role은 prefix로 ROLE_을 가져야만 Security에서 인식한다. hasRole은 ROLE_을 자동으로 prefix로 붙여 사용자의 권한을 체크한다.
                .antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/login/**", "/register/**").not().authenticated()

                .anyRequest().permitAll()

                //form을 이용한 자체 로그인
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login/process")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")

                //OAuth2 로그인
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                        .userService(principalOAuth2UserService);
    }
}
