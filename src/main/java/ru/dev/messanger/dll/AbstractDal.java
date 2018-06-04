package ru.dev.messanger.dll;

import ru.dev.messanger.entities.*;
import ru.dev.messanger.entities.MessageDTO;
import ru.dev.messanger.entities.UserDTO;

public interface AbstractDal {
    UserDTO authorization(String login, String password);
    Boolean emailAlreadyExists(String email);
    Boolean loginAlreadyExists(String login);
    Boolean setUser(NewUserDTO item);
    UserDTO getUser(int id);
    NewUserDTO getPUser(int id);
    Boolean updateUser(NewUserDTO item, Integer id);
    Boolean deleteUser(UserDTO item);
    Iterable<UserDTO> searchUsers(String searchQuery);
    Iterable<MessageConversationDTO> getConversations(Integer id);
    Iterable<MessageWithUnreadDTO> getDialogs(Integer id);
    Integer setConversation(ConversationDTO item);
    Integer setMessage(SentMessageDTO msg);
    Iterable<MessageDTO> getMessages(Integer conversation_id, Integer id, Integer message_id);
    Iterable<MessageDTO> searchInConversation(String searchQuery, Integer conversation_id);
    Iterable<SimpleConversationDTO> searchConversations(String searchQuery);
    Boolean joinTheConversation(Integer conversation_id, Integer id);
    Boolean leaveTheConversation(Integer conversation_id, Integer id);
    Boolean setUnreadMessages(Integer conversation_id, Integer id, Integer count);
    Boolean deleteConversation(Integer conversation_id, Integer id);
    Boolean setStatusOnline(Integer id, Integer status);
}
