package ru.dev.messanger.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import ru.dev.messanger.dll.Database;
import ru.dev.messanger.entities.ConversationDTO;
import ru.dev.messanger.entities.NewUserDTO;
import ru.dev.messanger.entities.SentMessageDTO;

import javax.validation.constraints.Max;
import java.util.*;

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
            @RequestParam String title,
            @RequestParam String users
    ) {
        ConversationDTO conversation = new ConversationDTO();
        conversation.setAdmin_id(admin_id);
        conversation.setTitle(title);
        String[] userNames = users.split(",");
        List<Integer> userIds = new ArrayList<Integer>(userNames.length);
        //TODO: validation
        try {
            for (int i = 0; i < userNames.length; i++) {
                userIds.add(Integer.parseInt(userNames[i]));
            }
        } catch (Exception e) {
            System.out.println("/setConversation parse users error");
        }
        conversation.setParticipants_id(userIds);

        return new Gson().toJson(Database.INSTANCE.setConversation(conversation));
    }

    @RequestMapping(value = "/setMessage", method = RequestMethod.GET, produces = "application/json")
    public String setMessage(
            @RequestParam int from_id,
            @RequestParam int conversation_id,
            @RequestParam String message,
            @RequestParam String attachment_url
    ) {
        SentMessageDTO messageDTO = new SentMessageDTO();
        messageDTO.setFrom_id(from_id);
        messageDTO.setConversation_id(conversation_id);
        messageDTO.setMessage(message);
        messageDTO.setAttachment_url(attachment_url);

        return new Gson().toJson(Database.INSTANCE.setMessage(messageDTO));
    }

    @RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = "application/json")
    public String getMessages(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id,
            @RequestParam Integer message_id
    ) {
        return new Gson().toJson(Database.INSTANCE.getMessages(conversation_id, id, message_id));
    }

    @RequestMapping(value = "/searchInConversation", method = RequestMethod.GET, produces = "application/json")
    public String searchInConversation(
            @RequestParam String searchQuery,
            @RequestParam Integer conversation_id
    ) {
        return new Gson().toJson(Database.INSTANCE.searchInConversation(searchQuery, conversation_id));
    }

    @RequestMapping(value = "/searchConversations", method = RequestMethod.GET, produces = "application/json")
    public String searchConversations(
            @RequestParam String searchQuery
    ) {
        return new Gson().toJson(Database.INSTANCE.searchConversations(searchQuery));
    }

    @RequestMapping(value = "/joinTheConversation", method = RequestMethod.GET, produces = "application/json")
    public String joinTheConversation(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id
    ) {
        return new Gson().toJson(Database.INSTANCE.joinTheConversation(conversation_id, id));
    }

    @RequestMapping(value = "/leaveTheConversation", method = RequestMethod.GET, produces = "application/json")
    public String leaveTheConversation(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id
    ) {
        return new Gson().toJson(Database.INSTANCE.leaveTheConversation(conversation_id, id));
    }

    @RequestMapping(value = "/setUnreadMessages", method = RequestMethod.GET, produces = "application/json")
    public String setUnreadMessages(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id,
            @RequestParam Integer count
    ) {
        return new Gson().toJson(Database.INSTANCE.setUnreadMessages(conversation_id, id, count));
    }

    @RequestMapping(value = "/deleteConversation", method = RequestMethod.GET, produces = "application/json")
    public String deleteConversation(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id
    ) {
        return new Gson().toJson(Database.INSTANCE.deleteConversation(conversation_id, id));
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET, produces = "application/json")
    public String setStatusOnline(
            @RequestParam Integer id,
            @RequestParam Integer status
    ) {
        return new Gson().toJson(Database.INSTANCE.setStatusOnline(id, status));
    }
}
