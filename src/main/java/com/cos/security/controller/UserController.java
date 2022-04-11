package com.cos.security.controller;
import com.cos.security.dto.ResponseDto;
import com.cos.security.model.User;
import com.cos.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register/process")
    public String register(User user, Model model){
        log.info(user.getUsername()+user.getPassword()+user.getEmail());
        ResponseDto<String> response = userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String user(Model model) {
        return "";
    }

}
