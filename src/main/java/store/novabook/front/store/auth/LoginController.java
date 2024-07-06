package store.novabook.front.store.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginController {
    @GetMapping
    public String getLoginForm() {
        return "store/auth/login_form";
    }
}