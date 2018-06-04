package ru.dev.messanger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.dev.messanger.BLL.BLL;
import ru.dev.messanger.dll.Database;
import ru.dev.messanger.service.UserSevice;

@Controller
public class RegistrationController {

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = UserSevice.activateUser(code);

        return isActivated ? "login" : "No such Code";
    }
}
