package ru.dev.messanger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dev.messanger.BLL.BLL;

@Controller
public class MappingController {

    private final BLL bll;

    public MappingController(BLL bll) {
        this.bll = bll;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String signin() {
        return "EnterPage";
    }

    @GetMapping("/main")
    public String main(@RequestParam String token) {
        return bll.checkToken(token) ? "redirect:/main" : "redirect:/signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "registration";
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String token) {
        bll.removeToken(token);
        return "redirect:/signin";
    }
}
