package ru.dev.messanger.BLL;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.dev.messanger.dll.Database;
import ru.dev.messanger.entities.*;
import ru.dev.messanger.service.UserService;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class BLL {

    private final UserService userService;  //TODO: НОРМАЛЬНО АВТОВАЙРИТЬ В ЛОГИКЕ?

    public BLL(UserService userService) {
        this.userService = userService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    private HashMap<Object, Token> userToken = new HashMap<>(); //Key(Object) is ID of User

    public HashMap<Object, Token> getUserToken() {
        return userToken;
    }

    public void addTokenToUser(TUser user, Token token) {
        this.userToken.put(user.getId(), token);
    }

    public Boolean checkToken(String token) {
        System.out.println(token);
        if ((token == null) || ("[object Object]".equals(token))) { //TODO: это ломает всю защиту | заглушка, чтобы войти   token == null
            return true;
        }

        if ((userToken.size() == 0) || (token.isEmpty()) || (token == null)) { // TODO: Can be removed (presents for better understanding)
            return false;
        }
        Token storedToken = getToken(token);
        if (storedToken != null) {
            if (storedToken.getExpires().compareTo(Instant.now()) >= 0) {
                storedToken.setExpires(Instant.now().plusSeconds(Token.LIFETIME));
                return true;
            } else {
                removeToken(storedToken);
                return false;
            }
        } else {
            return false;
        }
    }

    private Token getToken(String token) {
        for (Object key : userToken.keySet()) {
            if (token.equals(userToken.get(key).getStringToken())) {
                return userToken.get(key);
            }
        }
        return null;
    }

    public Boolean removeToken(Token token) {
        if (userToken.containsValue(token)) {
            for (Object key : userToken.keySet()) {
                if (userToken.get(key) == token) {
                    userToken.remove(key);
                    return true; //Tokens are unique, no need to continue iteration
                }
            }
        }
        return false;
    }

    public Boolean removeToken(String token) {
        for (Object key : userToken.keySet()) {
            if (token.equals(userToken.get(key).getStringToken())) {
                userToken.remove(key);
                return true; //Tokens are unique, no need to continue iteration
            }
        }
        return false;
    }

    public void removeUsersToken(TUser user) {
        this.userToken.remove(user.getId());
    }


    public static NewUserDTO getUserByACode(String code) {
        return Database.INSTANCE.getUserByACode(code);
    }

    public String authorization(String login, String password) {
        if (loginAlreadyExists(login) == "false") {
            return "No Such User"; //TODO: связана с ранним запросом ДО авторизации loginaleready exists И НЕ ТЕСТИТСЯ ИБО НИКАК чина давай
        }

        UserDTO user = Database.INSTANCE.authorization(login, Encoder.hash256(password));
        if (user == null) {
            return new Gson().toJson("not activated");
        }
        if (user.getActivation_code() == null) {
            TUser tuser = new TUser(user);
            addTokenToUser(tuser, tuser.getToken());
            return new Gson().toJson(tuser);
        } else {
            return new Gson().toJson("User is not activated yet"); //TODO: такое себе
        }
    }

    public String emailAlreadyExists(String email) {
        return new Gson().toJson(Database.INSTANCE.emailAlreadyExists(email));
    }

    public String loginAlreadyExists(String login) {
        return new Gson().toJson(Database.INSTANCE.loginAlreadyExists(login));
    }

    public Boolean setUser(NewUserDTO user) {
        return Database.INSTANCE.setUser(user);
    }

    public static Boolean updateActivation(NewUserDTO item) {
        return Database.INSTANCE.updateActivation(item);
    }

    public String setUser(
            String email,
            String login,
            String password,
            String first_name,
            String last_name,
            String sex,
            String status,
            String avatar) {
        NewUserDTO user = new NewUserDTO();
        user.setEmail(email);
        user.setLogin(login);
        user.setPassword(Encoder.hash256(password));
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setSex(sex);
        user.setStatus(status);
        user.setAvatar_url(avatar);
        user.setCreated_at(Instant.now());

        user.setActivation_code(UUID.randomUUID().toString());
        userService.sendActivationEmail(user);

        return new Gson().toJson(Database.INSTANCE.setUser(user));
    }

    public String getUser(int id) {
        return new Gson().toJson(Database.INSTANCE.getUser(id));
    }

    public NewUserDTO getPUser(int id) {
        return Database.INSTANCE.getPUser(id);
    }

    public String updateUser(
            int id,
            String password,
            String first_name,
            String last_name,
            String sex,
            String status,
            String avatar
    ) {
        NewUserDTO user = new NewUserDTO();
        user.setId(id);
        user.setPassword(Encoder.hash256(password));
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setSex(sex);
        user.setStatus(status);
        user.setAvatar_url(avatar);
        return new Gson().toJson(Database.INSTANCE.updateUser(user, id));
    }

    public String deleteUser(
            int id
    ) {
        return new Gson().toJson(Database.INSTANCE.deleteUser(id));
    }

    public String searchUsers(
            String searchQuery
    ) {
        return new Gson().toJson(Database.INSTANCE.searchUsers(searchQuery));
    }

    public String getConversations(
            int id
    ) {
        return new Gson().toJson(Database.INSTANCE.getConversations(id));
    }

    public String getDialogs(
            int id
    ) {
        return new Gson().toJson(Database.INSTANCE.getDialogs(id));
    }

    public String setConversation(
            int admin_id,
            String title,
            String users
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

    public String setMessage(
            int from_id,
            int conversation_id,
            String message,
            String attachment_url,
            MultipartFile file
    ) {
        String resultFilename = "";
        SentMessageDTO messageDTO = new SentMessageDTO();

        if (file != null && !file.getOriginalFilename().isEmpty()) {

            File uploadDir = new File("//" + uploadPath); //TODO:autowire this
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + "." + file.getOriginalFilename();

            try {
                //  file.transferTo(new File(uploadPath + "\\" + resultFilename));
                file.transferTo(new File(uploadPath + "\\" + resultFilename)); //TODO: move to properties
                messageDTO.setAttachment_url("img/uploads/" + resultFilename); //TODO: move to properties
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        messageDTO.setFrom_id(from_id);
        messageDTO.setConversation_id(conversation_id);
        messageDTO.setMessage(message);
        return new Gson().toJson(Database.INSTANCE.setMessage(messageDTO));
    }

    public String getMessages(
            Integer conversation_id,
            Integer id,
            Integer message_id
    ) {
        return new Gson().toJson(Database.INSTANCE.getMessages(conversation_id, id, message_id));
    }

    public String searchInConversation(
            String searchQuery,
            Integer conversation_id
    ) {
        return new Gson().toJson(Database.INSTANCE.searchInConversation(searchQuery, conversation_id));
    }

    public String searchConversations(
            String searchQuery
    ) {
        return new Gson().toJson(Database.INSTANCE.searchConversations(searchQuery));
    }

    public String joinTheConversation(
            Integer conversation_id,
            Integer id
    ) {
        return new Gson().toJson(Database.INSTANCE.joinTheConversation(conversation_id, id));
    }

    public String leaveTheConversation(
            Integer conversation_id,
            Integer id
    ) {
        return new Gson().toJson(Database.INSTANCE.leaveTheConversation(conversation_id, id));
    }

    public String setUnreadMessages(
            Integer conversation_id,
            Integer id,
            Integer count
    ) {
        return new Gson().toJson(Database.INSTANCE.setUnreadMessages(conversation_id, id, count));
    }

    public String deleteConversation(
            Integer conversation_id,
            Integer id
    ) {
        return new Gson().toJson(Database.INSTANCE.deleteConversation(conversation_id, id));
    }

    public String setStatusOnline(Integer id, Integer status) {
        return new Gson().toJson(Database.INSTANCE.setStatusOnline(id, status));
    }

}
