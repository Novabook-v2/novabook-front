package com.nhnacademy.novabook_front.store.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@Controller
public class UserController {

    @GetMapping("/user/form")
    public String getUserForm() {
        return "/store/auth/user_form";
    }

    @PostMapping
    public String createUser() {
        return "";
    }

}
