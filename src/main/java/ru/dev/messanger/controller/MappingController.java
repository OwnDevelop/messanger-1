package ru.dev.messanger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dev.messanger.BLL.BLL;

@Controller
public class MappingController {

    @GetMapping("/")
    public String main() {
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String signin() {
        return "EnterPage";
    }

    @GetMapping("/main")
    public String application() {
        return "main";
    }

    @GetMapping("/signup")
    public String signup() {
        return "registration";
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String token) {
        BLL.INSTANCE.removeToken(token);
        return "redirect:/signin";
    }
}
