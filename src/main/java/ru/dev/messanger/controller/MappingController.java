package ru.dev.messanger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

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

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/signin";
    }
}
