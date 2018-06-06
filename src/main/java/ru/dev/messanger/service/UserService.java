package ru.dev.messanger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.dev.messanger.BLL.BLL;
import ru.dev.messanger.entities.NewUserDTO;

@Service
public class UserService{

    @Autowired
    private  MailSender mail;

    @Value("${host.url}")
    private String hostUrl;

    public boolean sendActivationEmail(NewUserDTO user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Messenger. Please, visit next link: " + hostUrl + "/activate/%s",
                    user.getFirstName(),
                    user.getActivation_code()
            );

            mail.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        NewUserDTO user = BLL.getUserByACode(code);
        if (user == null) {
            return false;
        }
        user.setActivation_code(null);
        return BLL.updateActivation(user);
    }
}
