package ru.dev.messanger.dll;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dev.messanger.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

@Service
public class Database implements AbstractDal {

    private Properties properties;
    private String url;
    private final String DEFAULT_STATUS = "1";

    @Value("${database.user}")
    private String NAMEUSER;

    @Value("${database.password}")
    private String PASSWORD;

    @Value("${database.url}")
    private String URL;

    public Database() {

        properties = new Properties();
        properties.setProperty("url", "jdbc:mariadb://localhost:3306/messenger?useUnicode=yes&characterEncoding=UTF-8");
        properties.setProperty("jdbc.driver", "org.mariadb.jdbc.Driver");
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        url = "jdbc:mariadb://localhost:3306/messenger?useUnicode=yes&characterEncoding=UTF-8";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found.");
            e.printStackTrace();
        }
    }

    /// Создаёт конекшн
    private ResultSet getResult(String statement) throws SQLException {
        try (Connection connection = getConnection(URL,NAMEUSER,PASSWORD)){
            if (!connection.isClosed()) {
                System.out.println("Connected!");
                try (PreparedStatement st = connection.prepareStatement(statement)) {
                    st.executeQuery();
                    return st.getResultSet();
                }
            }
        }
        return null;
    }

    @Override
    public UserDTO authorization(String login, String password) {
        UserDTO user = null;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT users.id, email, login, first_name, last_name, sex, created_at, activation_code, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "WHERE (login='" + login + "' OR email='" + login + "') AND password='" + password + "';";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    user = new UserDTO();
                    while (rs.next()) {
                        user = getAUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        if (user != null && user.getId() == 0) {
            return null;
        }
        return user;
    }

    @Override
    public Boolean emailAlreadyExists(String email) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT  COUNT(id) FROM users WHERE email='" + email + "'";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        if (rs.getInt(1) > 0) {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean loginAlreadyExists(String login) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT COUNT (id) FROM users WHERE login='" + login + "'";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        if (rs.getInt(1) > 0) {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean setUser(NewUserDTO item) {
        Integer avatar_id = this.addImage(item.getAvatar_url());
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "INSERT INTO users (email, login, password, first_name, last_name, sex, status, avatar, activation_code) " +
                    "VALUES ('" + item.getEmail() + "', '" + item.getLogin() + "', '" + item.getPassword() + "', '" + item.getFirstName() + "', '" + item.getLastName() +
                    "', '" + item.getSex() + "', '" +
                    DEFAULT_STATUS + "', '" + avatar_id + "', '" + item.getActivation_code() + "');";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public UserDTO getUser(int id) {
        UserDTO user = null;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT users.id, email, login, first_name, last_name, sex, created_at, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "WHERE users.id=" + id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        user = getUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public NewUserDTO getPUser(int id) {
        NewUserDTO user = null;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT users.id, email, login, password, first_name, last_name, sex, created_at, activation_code, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "WHERE users.id=" + id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        user = getPUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public Boolean updateActivation(NewUserDTO item) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "UPDATE users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "SET activation_code = " + item.getActivation_code() + " WHERE users.id='" + item.getId() + "';";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean updateUser(NewUserDTO item, Integer id) {
        Integer avatar_id = this.addImage(item.getAvatar_url());
        System.out.println(String.valueOf(item.getStatusInt()));
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "UPDATE users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "SET password='" + item.getPassword() + "', first_name='" + item.getFirstName() + "', last_name='" + item.getLastName() +
                    "', sex='" + item.getSex() + "', status='" + item.getStatusInt() + "', avatar='" + avatar_id +
                    "' WHERE users.id='" + id + "';";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean deleteUser(int id) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "DELETE FROM users WHERE id=" + id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Iterable<UserDTO> searchUsers(String searchQuery) {
        List<UserDTO> users = null;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT users.id, email, login, first_name, last_name, sex, created_at, VALUE AS status, url AS avatar " +
                    "FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "WHERE login LIKE '%" + searchQuery + "%'";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) { //Что получаем
                    users = new ArrayList<>();
                    while (rs.next()) {
                        users.add(getUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Integer setConversation(ConversationDTO item) {
        int conversation_id = 0;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "INSERT INTO conversations (admin_id, title) " +
                    "VALUES ('" + item.getAdmin_id() + "', '" + item.getTitle() + "');";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                System.out.println("Connection problem.");
                e.printStackTrace();
            }
            SqlQuery = "SELECT id FROM conversations WHERE admin_id='" + item.getAdmin_id() + "' ORDER BY id DESC LIMIT 1";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        conversation_id = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Connection problem.");
                e.printStackTrace();
            }

            addParticipants(conversation_id, item.getParticipants_id());
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return conversation_id;
    }

    @Override
    public Integer setMessage(SentMessageDTO msg) {
        Integer id = 0;
        Integer image_id = this.addImage(msg.getAttachment_url());
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "INSERT INTO messages (conversation_id, from_id, message, attachment_id) " +
                    "VALUES ('" + msg.getConversation_id() + "', '" + msg.getFrom_id() + "', '" + msg.getMessage() + "', '" + image_id + "')";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            }
            SqlQuery = "SELECT id FROM messages WHERE conversation_id=" + msg.getConversation_id() + " AND from_id=" + msg.getFrom_id() + " ORDER BY messages.id DESC LIMIT 1";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public Iterable<MessageDTO> getMessages(Integer conversation_id, Integer id, Integer message_id) {
        String SqlQuery = "SELECT messages.id, messages.conversation_id, from_id, message, messages.created_at, url AS attachment_url " +
                "FROM messages LEFT JOIN photos ON messages.attachment_id=photos.id " +
                "LEFT JOIN deleted_conversations ON messages.conversation_id=deleted_conversations.conversation_id " +
                "WHERE messages.conversation_id=" + conversation_id + " AND messages.id<=" + message_id +
                " AND (messages.created_at>deleted_conversations.deleted_at OR deleted_conversations.user_id IS NULL OR deleted_conversations.user_id!=" + id +
                ") ORDER BY messages.id DESC LIMIT 10;";
        List<MessageDTO> messages = getMessages(SqlQuery);

        return messages;
    }

    private List<MessageDTO> getMessages(String sqlQuery) {
        List<MessageDTO> messages = null;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(sqlQuery)) {
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) {
                    messages = new ArrayList<>();
                    while (rs.next()) {
                        messages.add(getMessage(rs));
                    }
                }
            }
            int k = 0;
            for (MessageDTO i : messages) {
                sqlQuery = "SELECT first_name, last_name, url AS avatar_url FROM users LEFT JOIN messages ON users.id=messages.from_id LEFT JOIN photos ON users.avatar=photos.id " +
                        "WHERE messages.id=" + i.getId();
                try (PreparedStatement st = connection.prepareStatement(sqlQuery)) {
                    st.executeQuery();

                    try (ResultSet rs = st.getResultSet()) {
                        while (rs.next()) {
                            MessageDTO msg = messages.get(k);
                            msg.setFirst_name(rs.getString(1));
                            msg.setLast_name(rs.getString(2));
                            msg.setImage_url(rs.getString(3));
                            messages.set(k, msg);
                        }
                    }
                }
                k++;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Iterable<MessageDTO> searchInConversation(String searchQuery, Integer conversation_id) {
        String SqlQuery = "SELECT messages.id, messages.conversation_id, from_id, message, messages.created_at, url AS attachment_url " +
                "FROM messages LEFT JOIN photos ON messages.attachment_id=photos.id " +
                "WHERE messages.conversation_id=" + conversation_id + " AND message LIKE '%" + searchQuery + "%' ORDER BY messages.id DESC LIMIT 100;";

        List<MessageDTO> messages = getMessages(SqlQuery);
        return messages;
    }


    @Override
    public Iterable<SimpleConversationDTO> searchConversations(String searchQuery) {
        List<SimpleConversationDTO> conversations = null;

        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT id, title, created_at " +
                    "FROM conversations " +
                    "WHERE title LIKE '%" + searchQuery + "%';";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    conversations = new ArrayList<>();
                    while (rs.next()) {
                        ConversationDTO item = new ConversationDTO();
                        item.setId(rs.getInt(1));
                        item.setTitle(rs.getString(2));
                        item.setCreated_at(rs.getTimestamp(3).toInstant());
                        conversations.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return conversations;
    }

    @Override
    public Boolean joinTheConversation(Integer conversation_id, Integer id) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "INSERT INTO participants (conversation_id, user_id, unread_messages) " +
                    "VALUES ('" + conversation_id + "', '" + id + "', 0);";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            }
        } catch (SQLException e) {
            System.out.println("Connection problem. (bad id's in joinTheConversation)");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean leaveTheConversation(Integer conversation_id, Integer id) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT COUNT (user_id) FROM participants WHERE conversation_id=" + conversation_id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        if (rs.getInt(1) > 3) {
                            SqlQuery = "DELETE FROM participants WHERE user_id=" + id + " AND conversation_id=" + conversation_id;
                            try (PreparedStatement stm = connection.prepareStatement(SqlQuery)) {
                                stm.executeQuery();
                                return true;
                            }
                        } else return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean setUnreadMessages(Integer conversation_id, Integer id, Integer count) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "UPDATE participants SET unread_messages='" + count + "' " +
                    "WHERE conversation_id=" + conversation_id + " AND user_id=" + id + ";";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem. (bad id's ot count in setUnreadMessages)");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteConversation(Integer conversation_id, Integer id) {
        String SqlQuery;
        Boolean exist = false;
        Integer deletedId = 0;

        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            SqlQuery = "SELECT COUNT (id) FROM deleted_conversations WHERE conversation_id=" + conversation_id + " AND user_id= " + id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        if (rs.getInt(1) > 0) {
                            exist = true;
                        }
                    }
                }
            }


            if (exist) {
                SqlQuery = "UPDATE deleted_conversations SET deleted_at=NOW()" +
                        "WHERE conversation_id=" + conversation_id + " AND user_id=" + id + ";";
            } else {
                SqlQuery = "INSERT INTO deleted_conversations (conversation_id, user_id) " +
                        "VALUES ('" + conversation_id + "', '" + id + "');";

            }
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            }

            SqlQuery = "SELECT COUNT (user_id) FROM deleted_conversations WHERE conversation_id=" + conversation_id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        deletedId = rs.getInt(1);
                    }
                }
            }

            SqlQuery = "SELECT COUNT (user_id) FROM participants WHERE conversation_id=" + conversation_id;
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        if (rs.getInt(1) == deletedId) {
                            SqlQuery = "DELETE FROM conversations WHERE id=" + conversation_id;
                            try (PreparedStatement stm = connection.prepareStatement(SqlQuery)) {
                                stm.executeQuery();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean setStatusOnline(Integer id, Integer status) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "UPDATE users SET status='" + status + "' WHERE id=" + id + ";";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public NewUserDTO getUserByACode(String code) {
        NewUserDTO user = null;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT users.id, email, login, password, first_name, last_name, sex, created_at, status, avatar, activation_code, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "WHERE users.activation_code = ?";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, code);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        user = getFullUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Iterable<MessageConversationDTO> getConversations(Integer id) {
        List<MessageConversationDTO> conversations = null;
        List<Integer> conversations_id = null;

        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT participants.conversation_id FROM participants " +
                    "LEFT JOIN deleted_conversations on participants.conversation_id=deleted_conversations.conversation_id " +
                    "WHERE participants.user_id=" + id + " AND (deleted_conversations.user_id !=" + id + " OR deleted_conversations.user_id IS NULL)";

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    conversations_id = new ArrayList<>();
                    while (rs.next()) {
                        conversations_id.add(rs.getInt(1));
                    }
                }
            }
            conversations = new ArrayList<>();
            for (int i : conversations_id) {
                SqlQuery = "SELECT COUNT (user_id) FROM participants WHERE conversation_id=" + i;

                try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                    st.executeQuery();

                    try (ResultSet rs = st.getResultSet()) {
                        while (rs.next()) {
                            SqlQuery = "UPDATE messages LEFT JOIN participants ON messages.conversation_id=participants.conversation_id " +
                                    "SET messages.to_id='" + id +
                                    "' WHERE messages.conversation_id=" + i + " AND user_id=" + id;

                            try (PreparedStatement str = connection.prepareStatement(SqlQuery)) {
                                str.executeQuery();
                            }
                            if (rs.getInt(1) > 2) {
                                SqlQuery = "SELECT messages.id, messages.conversation_id, title, from_id, url as avatar_url, message, messages.created_at, unread_messages " +
                                        "FROM messages LEFT JOIN conversations ON messages.conversation_id=conversations.id " +
                                        "LEFT JOIN users ON messages.from_id=users.id LEFT JOIN photos ON users.avatar=photos.id " +
                                        "LEFT JOIN participants ON participants.user_id=messages.to_id " +
                                        "WHERE messages.conversation_id=" + i + " AND user_id=" + id + " ORDER BY messages.id DESC LIMIT 1;";

                                try (PreparedStatement stm = connection.prepareStatement(SqlQuery)) {
                                    stm.executeQuery();
                                    try (ResultSet rst = stm.getResultSet()) {
                                        while (rst.next()) {
                                            MessageConversationDTO msg = new MessageConversationDTO();
                                            msg.setId(rst.getInt(1));
                                            msg.setConversation_id(rst.getInt(2));
                                            msg.setTitle(rst.getString(3));
                                            msg.setFrom_id(rst.getInt(4));
                                            msg.setImage_url(rst.getString(5));
                                            msg.setMessage(rst.getString(6));
                                            msg.setCreated_at(rst.getTimestamp(7).toInstant());
                                            msg.setCountUnread(rst.getInt(8));

                                            conversations.add(msg);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return conversations;
    }

    @Override
    public Iterable<MessageWithUnreadDTO> getDialogs(Integer id) {
        List<MessageWithUnreadDTO> conversations = null;
        List<Integer> conversations_id = null;

        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT participants.conversation_id FROM participants " +
                    "LEFT JOIN deleted_conversations on participants.conversation_id=deleted_conversations.conversation_id " +
                    "WHERE participants.user_id=" + id + " AND (deleted_conversations.user_id !=" + id + " OR deleted_conversations.user_id IS NULL)";

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    conversations_id = new ArrayList<>();
                    while (rs.next()) {
                        conversations_id.add(rs.getInt(1));
                    }
                }
            }

            conversations = new ArrayList<>();
            for (int i : conversations_id) {
                SqlQuery = "SELECT COUNT (user_id) FROM participants WHERE conversation_id=" + i;

                try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                    st.executeQuery();

                    try (ResultSet rs = st.getResultSet()) {
                        while (rs.next()) {
                            SqlQuery = "UPDATE messages LEFT JOIN participants ON messages.conversation_id=participants.conversation_id " +
                                    "SET messages.to_id='" + id +
                                    "' WHERE messages.conversation_id=" + i + " AND user_id=" + id;

                            try (PreparedStatement str = connection.prepareStatement(SqlQuery)) {
                                str.executeQuery();
                            }

                            if (rs.getInt(1) == 2) {

                                SqlQuery = "SELECT conversation_id, users.id, first_name, last_name, url as avatar_url " +
                                        "FROM users LEFT JOIN participants ON users.id=participants.user_id LEFT JOIN photos ON users.avatar=photos.id " +
                                        "WHERE conversation_id=" + i + " AND user_id!=" + id;
                                try (PreparedStatement stm = connection.prepareStatement(SqlQuery)) {
                                    stm.executeQuery();

                                    try (ResultSet rst = stm.getResultSet()) {
                                        while (rst.next()) {
                                            MessageWithUnreadDTO msg = new MessageWithUnreadDTO();
                                            msg.setConversation_id(rst.getInt(1));
                                            msg.setFrom_id(rst.getInt(2));
                                            msg.setFirst_name(rst.getString(3));
                                            msg.setLast_name(rst.getString(4));
                                            msg.setImage_url(rst.getString(5));
                                            conversations.add(msg);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int k = 0;
            for (MessageWithUnreadDTO i : conversations) {
                SqlQuery = "SELECT messages.id, message, messages.created_at, url as attachment_id, unread_messages " +
                        "FROM messages LEFT JOIN photos ON messages.attachment_id=photos.id " +
                        "LEFT JOIN participants ON messages.conversation_id=participants.conversation_id " +
                        "WHERE messages.conversation_id=" + i.getConversation_id() + " AND user_id=" + id + " ORDER BY messages.id DESC LIMIT 1;";
                try (PreparedStatement stm = connection.prepareStatement(SqlQuery)) {
                    stm.executeQuery();
                    try (ResultSet rst = stm.getResultSet()) {
                        while (rst.next()) {
                            MessageWithUnreadDTO msg = conversations.get(k);
                            msg.setId(rst.getInt(1));
                            msg.setMessage(rst.getString(2));
                            msg.setCreated_at(rst.getTimestamp(3).toInstant());
                            msg.setAttachment_url(rst.getString(4));
                            msg.setCountUnread(rst.getInt(5));

                            conversations.set(k, msg);
                        }
                    }
                }
                k++;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return conversations;
    }

    private Boolean addParticipants(int id, List<Integer> participants) {
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;
            Boolean exist = false;

            if (participants == null || participants.size() == 0) {
                System.out.println("Cannot make new conversation without users");
                return false;
            }
            for (int i : participants) {
                SqlQuery = "SELECT COUNT (id) FROM participants WHERE conversation_id=" + id + " AND user_id= " + i;
                try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                    st.executeQuery();
                    try (ResultSet rs = st.getResultSet()) {
                        while (rs.next()) {
                            if (rs.getInt(1) > 0) {
                                exist = true;
                            }
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Connection problem.");
                    e.printStackTrace();
                }
                if (!exist) {
                    SqlQuery = "INSERT INTO participants (conversation_id, user_id, unread_messages) " +
                            "VALUES ('" + id + "', '" + i + "', '" + 0 + "')";
                    try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                        st.executeQuery();
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    private static UserDTO getAUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setId(rs.getInt(1));
        user.setEmail(rs.getString(2));
        user.setLogin(rs.getString(3));
        user.setFirstName(rs.getString(4));
        user.setLastName(rs.getString(5));
        user.setSex(rs.getString(6));
        user.setCreated_at(rs.getTimestamp(7).toInstant());
        user.setActivation_code(rs.getString(8));
        user.setStatus(rs.getString(9));
        user.setAvatar_url(rs.getString(10));

        return user;
    }

    private static UserDTO getUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setId(rs.getInt(1));
        user.setEmail(rs.getString(2));
        user.setLogin(rs.getString(3));
        user.setFirstName(rs.getString(4));
        user.setLastName(rs.getString(5));
        user.setSex(rs.getString(6));
        user.setCreated_at(rs.getTimestamp(7).toInstant());
        user.setStatus(rs.getString(8));
        user.setAvatar_url(rs.getString(9));
        return user;
    }

    private static NewUserDTO getFullUser(ResultSet rs) throws SQLException {
        NewUserDTO user = new NewUserDTO();
        user.setId(rs.getInt(1));
        user.setEmail(rs.getString(2));
        user.setLogin(rs.getString(3));
        user.setPassword(rs.getString(4));
        user.setFirstName(rs.getString(5));
        user.setLastName(rs.getString(6));
        user.setSex(rs.getString(7));
        user.setCreated_at(rs.getTimestamp(8).toInstant());
        user.setStatus(rs.getString(9));
        user.setAvatar_url(rs.getString(10));
        user.setActivation_code(rs.getString(11));
        return user;
    }

    private static NewUserDTO getPUser(ResultSet rs) throws SQLException {
        NewUserDTO user = new NewUserDTO();
        user.setId(rs.getInt(1));
        user.setEmail(rs.getString(2));
        user.setLogin(rs.getString(3));
        user.setPassword(rs.getString(4));
        user.setFirstName(rs.getString(5));
        user.setLastName(rs.getString(6));
        user.setSex(rs.getString(7));
        user.setCreated_at(rs.getTimestamp(8).toInstant());
        user.setActivation_code(rs.getString(9));
        user.setStatus(rs.getString(10));
        user.setAvatar_url(rs.getString(11));
        return user;
    }

    private static MessageDTO getMessage(ResultSet rs) throws SQLException {
        MessageDTO msg = new MessageDTO();
        msg.setId(rs.getInt(1));
        msg.setConversation_id(rs.getInt(2));
        msg.setFrom_id(rs.getInt(3));
        msg.setMessage(rs.getString(4));
        msg.setCreated_at(rs.getTimestamp(5).toInstant());
        msg.setAttachment_url(rs.getString(6));
        return msg;
    }

    public Integer addImage(String url) {
        Integer id = 0;
        try (Connection connection = getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery = "SELECT id FROM photos WHERE url='" + url + "';";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) { //Что получаем
                    while (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }
            if (id == 0) {
                SqlQuery = "INSERT INTO photos (url) VALUES ('" + url + "');";
                try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                    st.executeQuery();
                }
                SqlQuery = "SELECT id FROM photos WHERE url='" + url + "';";
                try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                    st.executeQuery();
                    try (ResultSet rs = st.getResultSet()) { //Что получаем
                        while (rs.next()) {
                            id = rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public Boolean setStatusOfflineeAll() {
        try {
            String SqlQuery = "UPDATE users SET status='" + DEFAULT_STATUS + "';";
            getResult(SqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public NewUserDTO getFullUser(int id) {
        NewUserDTO user = null;
        String SqlQuery = "SELECT users.id, email, login, password, first_name, last_name, sex, created_at, activation_code, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                "WHERE users.id=" + id;
        try {
            ResultSet rs = getResult(SqlQuery);
            while (rs.next()) {
                user = getFullUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}