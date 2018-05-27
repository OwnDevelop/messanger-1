package ru.dev.messanger.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dev.messanger.dll.Database;
import ru.dev.messanger.entities.ConversationDTO;
import ru.dev.messanger.entities.NewUserDTO;

import java.util.Date;
import java.util.UUID;

@RestController
public class RESTController {

    @RequestMapping(value = "/authorization", method = RequestMethod.GET, produces = "application/json")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        return new Gson().toJson(Database.INSTANCE.authorization(login, password));
    }

    @RequestMapping(value = "/emailAlreadyExists", method = RequestMethod.GET, produces = "application/json")
    public String emailAlreadyExists(@RequestParam String email) {
        return new Gson().toJson(Database.INSTANCE.emailAlreadyExists(email));
    }

    @RequestMapping(value = "/loginAlreadyExists", method = RequestMethod.GET, produces = "application/json")
    public String loginAlreadyExists(@RequestParam String login) {
        return new Gson().toJson(Database.INSTANCE.loginAlreadyExists(login));
    }

    @RequestMapping(value = "/setUser", method = RequestMethod.GET, produces = "application/json")
    public String setUser(
            @RequestParam String email,
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String first_name,
            @RequestParam String last_name,
            @RequestParam String sex,
            @RequestParam String status,
            @RequestParam String avatar) {
        NewUserDTO user = new NewUserDTO();
        user.setEmail(email);
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setSex(sex);
        user.setStatus(status);
        user.setAvatar_url(avatar);
        return new Gson().toJson(Database.INSTANCE.setUser(user));
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = "application/json")
    public String getUser(@RequestParam int id) {
        return new Gson().toJson(Database.INSTANCE.getUser(id));
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.GET, produces = "application/json")
    public String updateUser(
            @RequestParam int id,
            @RequestParam String password,
            @RequestParam String first_name,
            @RequestParam String last_name,
            @RequestParam String sex,
            @RequestParam String status,
            @RequestParam String avatar
            ) {
        NewUserDTO user = new NewUserDTO();
        user.setId(id);
        user.setPassword(password);
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setSex(sex);
        user.setStatus(status);
        user.setAvatar_url(avatar);

        return new Gson().toJson(Database.INSTANCE.updateUser(user, id));
    }
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET, produces = "application/json")
    public String deleteUser(
            @RequestParam int id
    ) {
        NewUserDTO user = new NewUserDTO();
        user.setId(id);
        return new Gson().toJson(Database.INSTANCE.deleteUser(user));
    }
    @RequestMapping(value = "/searchUsers", method = RequestMethod.GET, produces = "application/json")
    public String searchUsers(
            @RequestParam String searchQuery
    ) {
        return new Gson().toJson(Database.INSTANCE.searchUsers(searchQuery));
    }
  //АПАСНО
   @RequestMapping(value = "/getConversations", method = RequestMethod.GET, produces = "application/json")
    public String getConversations(
            @RequestParam int id
    ) {
        return new Gson().toJson(Database.INSTANCE.getConversations(id));
    }

  @RequestMapping(value = "/getDialogs", method = RequestMethod.GET, produces = "application/json")
  public String getDialogs(
          @RequestParam int id
  ) {
      return new Gson().toJson(Database.INSTANCE.getDialogs(id));
  }

    @RequestMapping(value = "/setConversation", method = RequestMethod.GET, produces = "application/json")
    public String setConversation(
            @RequestParam int admin_id,
            @RequestParam String title
            ) {
        ConversationDTO conversation = new ConversationDTO();
        conversation.setAdmin_id(admin_id);
        conversation.setTitle(title);
        conversation.setParticipants_id(null);

        return new Gson().toJson(Database.INSTANCE.setConversation(conversation));
    }
}
