package ru.dev.messanger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import ru.dev.messanger.BLL.BLL;
import ru.dev.messanger.entities.NewUserDTO;

public class UserSevice {

    @Autowired
    private static MailSender mailSender;

    @Value("${host-url}")
    private static String hostUrl;

    public static boolean sendActivationEmail(NewUserDTO user) {

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Messenger. Please, visit next link: " + hostUrl + "/activate/%s",
                    user.getFirstName(),
                    user.getActivation_code()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public static boolean activateUser(String code) {
        NewUserDTO user = BLL.INSTANCE.getUserByACode(code);
        if (user == null) {
            return false;
        }
        user.setActivation_code(null);
        return BLL.INSTANCE.updateActivation(user);
    }
}
