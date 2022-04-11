package com.cos.security.config.auth;

//시큐리티가 /login/process를 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료가 되면 session을 만들어줌 (Security Context Holder)
// 세션에 들어갈 수 있는 객체 = Authentication 타입 객체
// Authentication 객체 안엔 User정보가 있어야 함
// User정보의 타입은 UserDetails 타입이어야 함

// Security Session => Authentication => UserDetails

import com.cos.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

    // 해당 유저의 권한을 리턴하는 함수
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> user.getRole());
        return collection;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        // 회원이 1년간 활동이 없어 휴면계정으로 만들때
        // 현재시간-최근활동시간 > 1년 이라면 false로 바꿈
        return true;
    }
}
