package com.cos.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
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

                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login/process")
                    .defaultSuccessUrl("/")

                .and()
                    .logout()
                    .logoutSuccessUrl("/");
    }
}
