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

    @GetMapping("/signup")
    public String signup() {
        return "registration";
    }
}
