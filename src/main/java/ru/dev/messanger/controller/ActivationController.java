package ru.dev.messanger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.dev.messanger.BLL.BLL;
import ru.dev.messanger.service.UserService;

@Controller
public class ActivationController {

    @Autowired
    private UserService userService;
    @Autowired
    private BLL bll;


    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = bll.activateUser(code);

        return isActivated ? "redirect:/signin" : "No such Code";
    }
}
