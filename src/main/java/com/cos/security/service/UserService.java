package com.cos.security.service;

import com.cos.security.dto.ResponseDto;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public ResponseDto<String> register(User user){
        user.setRole("ROLE_USER");
        user.setProvider("Default");
        user.setProviderId(null);
        String rawPassword = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        userRepository.save(user);
        return ResponseDto.<String>builder()
                .status(HttpStatus.OK)
                .data("redirect:/login").build();
    }

}
