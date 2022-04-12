package com.cos.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        //Mustache의 기본 폴더: src/main/resources/
        //Spring에서 지원하는 mustache에 설정되는 뷰리졸버:  prefix-/templates/, suffix-.mustache
        return "index";
    }

    //인덱스 컨트롤러 명명법 전략
    //url과 탬플릿의 이름이 같으면 circular path excetion을 일으킴
    // "/"를 index로 명명하며 이후 도메인을 "-" 로 순차적으로 연결

    @GetMapping("/admin")
    public String admin() {
        return "index-admin";
    }
    @GetMapping("/manager")
    public String manager() {
        return "index-manager";
    }

    //로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "index-login";
    }
    //회원가입 페이지
   @GetMapping("/register")
    public String register() {
        return "index-register";
    }

}
