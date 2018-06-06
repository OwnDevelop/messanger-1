package ru.dev.messanger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.dev.messanger.BLL.BLL;


@RestController
public class RESTController {

    private final BLL bll;

    public RESTController(BLL bll) {
        this.bll = bll;
    }

    @RequestMapping(value = "/authorization", method = RequestMethod.GET, produces = "application/json")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        return bll.authorization(login, password);
    }

    @RequestMapping(value = "/emailAlreadyExists", method = RequestMethod.GET, produces = "application/json")
    public String emailAlreadyExists(@RequestParam String email) {
        return bll.emailAlreadyExists(email);
    }

    @RequestMapping(value = "/loginAlreadyExists", method = RequestMethod.GET, produces = "application/json")
    public String loginAlreadyExists(@RequestParam String login) {
        return bll.loginAlreadyExists(login);
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
        return bll.setUser(email, login, password, first_name, last_name, sex, status, avatar);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST, produces = "application/json")
    public String getUser(@RequestParam int id) {
        return bll.getUser(id);
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
        return bll.updateUser(id, password, first_name, last_name, sex, status, avatar);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET, produces = "application/json")
    public String deleteUser(@RequestParam int id) {
        return bll.deleteUser(id);
    }

    @RequestMapping(value = "/searchUsers", method = RequestMethod.POST, produces = "application/json")
    public String searchUsers(@RequestParam String searchQuery) {
        return bll.searchUsers(searchQuery);
    }

    @RequestMapping(value = "/getConversations", method = RequestMethod.POST, produces = "application/json")
    public String getConversations(@RequestParam int id) {
        return bll.getConversations(id);
    }

    @RequestMapping(value = "/getDialogs", method = RequestMethod.POST, produces = "application/json")
    public String getDialogs(@RequestParam int id) {
        return bll.getDialogs(id);
    }

    @RequestMapping(value = "/setConversation", method = RequestMethod.POST, produces = "application/json")
    public String setConversation(
            @RequestParam int admin_id,
            @RequestParam String title,
            @RequestParam String users
    ) {
        return bll.setConversation(admin_id, title, users);
    }

    @RequestMapping(value = "/setMessage", method = RequestMethod.POST, produces = "application/json")
    public String setMessage(
            @RequestParam int from_id,
            @RequestParam int conversation_id,
            @RequestParam String message,
            @RequestParam String attachment_url,
            @RequestParam("file") MultipartFile file

    ) {
        return bll.setMessage(from_id, conversation_id, message, attachment_url, file);
    }

    @RequestMapping(value = "/getMessages", method = RequestMethod.POST, produces = "application/json")
    public String getMessages(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id,
            @RequestParam Integer message_id
    ) {
        return bll.getMessages(conversation_id, id, message_id);
    }

    @RequestMapping(value = "/searchInConversation", method = RequestMethod.GET, produces = "application/json")
    public String searchInConversation(
            @RequestParam String searchQuery,
            @RequestParam Integer conversation_id
    ) {
        return bll.searchInConversation(searchQuery, conversation_id);
    }

    @RequestMapping(value = "/searchConversations", method = RequestMethod.POST, produces = "application/json")
    public String searchConversations(
            @RequestParam String searchQuery
    ) {
        return bll.searchConversations(searchQuery);
    }

    @RequestMapping(value = "/joinTheConversation", method = RequestMethod.POST, produces = "application/json")
    public String joinTheConversation(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id
    ) {
        return bll.joinTheConversation(conversation_id, id);
    }

    @RequestMapping(value = "/leaveTheConversation", method = RequestMethod.GET, produces = "application/json")
    public String leaveTheConversation(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id
    ) {
        return bll.leaveTheConversation(conversation_id, id);
    }

    @RequestMapping(value = "/setUnreadMessages", method = RequestMethod.POST, produces = "application/json")
    public String setUnreadMessages(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id,
            @RequestParam Integer count
    ) {
        return bll.setUnreadMessages(conversation_id, id, count);
    }

    @RequestMapping(value = "/deleteConversation", method = RequestMethod.GET, produces = "application/json")
    public String deleteConversation(
            @RequestParam Integer conversation_id,
            @RequestParam Integer id
    ) {
        return bll.deleteConversation(conversation_id, id);
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST, produces = "application/json")
    public String setStatusOnline(
            @RequestParam Integer id,
            @RequestParam Integer status
    ) {
        return bll.setStatusOnline(id, status);
    }
}
