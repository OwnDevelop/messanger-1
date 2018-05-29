package ru.dev.messanger.dll;

import ru.dev.messanger.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database implements AbstractDal {

    Properties properties;
    String url;

    public static final Database INSTANCE = new Database();   // SINGLETONE

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

    @Override
    public UserDTO authorization(String login, String password) {
        UserDTO user = null;
        String SqlQuery = "SELECT users.id, email, login, first_name, last_name, sex, created_at, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                "WHERE (login='" + login + "' OR email='" + login + "') AND password='" + password + "';";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                //st.setInt(1,2);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) { //Что получаем
                    user = new UserDTO();
                    while (rs.next()) {
                        user = getUser(rs);
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
        String SqlQuery = "SELECT  COUNT(id) FROM users WHERE email='" + email + "'";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
        String SqlQuery = "SELECT COUNT (id) FROM users WHERE login='" + login + "'";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;

            SqlQuery = "INSERT INTO users (email, login, password, first_name, last_name, sex, status, avatar) " +
                    "VALUES ('" + item.getEmail() + "', '" + item.getLogin() + "', '" + item.getPassword() + "', '" + item.getFirstName() + "', '" + item.getLastName() + "', '" + item.getSex() + "', '" + "1" + "', '" + avatar_id + "');";
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

        String SqlQuery = "SELECT users.id, email, login, first_name, last_name, sex, created_at, value as status, url as avatar FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                "WHERE users.id=" + id;
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                //st.setInt(1,2);
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
    public Boolean updateUser(NewUserDTO item, Integer id) {
        Integer avatar_id = this.addImage(item.getAvatar_url());
        String SqlQuery;

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            SqlQuery = "UPDATE users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                    "SET password='" + item.getPassword() + "', first_name='" + item.getFirstName() + "', last_name='" + item.getLastName() +
                    "', sex='" + item.getSex() + "', status='" + item.getStatus() + "', avatar='" + avatar_id +
                    "' WHERE users.id='" + id + "';";
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
    public Boolean deleteUser(UserDTO item) {
        String SqlQuery = "DELETE FROM users WHERE id=" + item.getId();
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                //st.setInt(1,2);
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

        String SqlQuery = "SELECT users.id, email, login, first_name, last_name, sex, created_at, VALUE AS status, url AS avatar " +
                "FROM users LEFT JOIN status ON users.status=status.id LEFT JOIN photos ON users.avatar=photos.id " +
                "WHERE login LIKE '%" + searchQuery + "%'";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                //st.setInt(1,2);
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
    public Boolean setConversation(ConversationDTO item) {
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;

            SqlQuery = "INSERT INTO conversations (admin_id, title) " +
                    "VALUES ('" + item.getAdmin_id() + "', '" + item.getTitle() + "');";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            } catch (SQLException e) {
                return false;
            }

            int conversation_id = 0;
            SqlQuery = "SELECT id FROM conversations WHERE admin_id='" + item.getAdmin_id() + "' ORDER BY id DESC LIMIT 1";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        conversation_id = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {

                return false;
            }
            addParticipants(conversation_id, item.getParticipants_id());

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public MessageDTO setMessage(SentMessageDTO msg) {
        Integer attachment_id = this.addImage(msg.getAttachment_url());
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;
            SqlQuery = "INSERT INTO messages (conversation_id, from_id, message, attachment_id) " +
                    "VALUES ('" + msg.getConversation_id() + "', '" + msg.getFrom_id() + "', '" + msg.getMessage() + "', '" + attachment_id + "')";
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<MessageDTO> getMessages(Integer conversation_id, Integer id, Integer message_id) {


        String SqlQuery = "SELECT messages.id, messages.conversation_id, from_id, message, messages.created_at, url AS attachment_url " +
                "FROM messages LEFT JOIN photos ON messages.attachment_id=photos.id " +
                "LEFT JOIN deleted_conversations ON messages.conversation_id=deleted_conversations.conversation_id " +
                "WHERE messages.conversation_id=" + conversation_id + " AND messages.id<=" + message_id +
                " AND (messages.created_at>deleted_conversations.deleted_at OR deleted_conversations.user_id IS NULL OR deleted_conversations.user_id!=" + id +
                ") LIMIT 10;";
        List<MessageDTO> messages = getMessages(SqlQuery);

        return messages;
    }

    private List<MessageDTO> getMessages(String sqlQuery) {
        List<MessageDTO> messages = null;
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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

        String SqlQuery = "SELECT id, title, created_at " +
                "FROM conversations " +
                "WHERE title LIKE '%" + searchQuery + "%';";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                //st.setInt(1,2);
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) { //Что получаем
                    conversations = new ArrayList<>();
                    while (rs.next()) {
                        ConversationDTO item = new ConversationDTO();
                        item.setId(rs.getInt(1));
                        item.setTitle(rs.getString(2));
                        item.setCreated_at(rs.getDate(3));
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

        String SqlQuery = "INSERT INTO participants (conversation_id, user_id, unread_messages) " +
                "VALUES ('" + conversation_id + "', '" + id + "', 0);";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
        String SqlQuery = "DELETE FROM participants WHERE user_id=" + id + " AND conversation_id=" + conversation_id;
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();
            }
        } catch (SQLException e) {
            System.out.println("Connection problem. (bad id's in leaveTheConversation)");
            e.printStackTrace();
            return false;

        }
        return true;
    }

    @Override
    public Boolean setUnreadMessages(Integer conversation_id, Integer id, Integer count) {
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

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

        SqlQuery = "SELECT COUNT (id) FROM deleted_conversations WHERE conversation_id=" + conversation_id + " AND user_id= " + id;
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        if (exist) {
            SqlQuery = "UPDATE deleted_conversations SET deleted_at=NOW()" +
                    "WHERE conversation_id=" + conversation_id + " AND user_id=" + id + ";";
        } else {
            SqlQuery = "INSERT INTO deleted_conversations (conversation_id, user_id) " +
                    "VALUES ('" + conversation_id + "', '" + id + "');";
        }
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
    public Boolean setStatusOnline(Integer id, Boolean now) {
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;
            if (now) {
                SqlQuery = "UPDATE users SET status='1' " +
                        "WHERE id=" + id + ";";
            } else {
                SqlQuery = "UPDATE users SET status='4' " +
                        "WHERE id=" + id + ";";
            }

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
    public Iterable<MessageConversationDTO> getConversations(Integer id) {
        List<MessageConversationDTO> conversations = null;

        List<Integer> conversations_id = null;
        String SqlQuery = "SELECT participants.conversation_id FROM participants " +
                "LEFT JOIN deleted_conversations on participants.conversation_id=deleted_conversations.conversation_id " +
                "WHERE participants.user_id=" + id + " AND (deleted_conversations.user_id !=" + id + " OR deleted_conversations.user_id IS NULL)";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
                            System.out.println(rs.getInt(1));
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
                                            msg.setCreated_at(rst.getDate(7));
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
        String SqlQuery = "SELECT participants.conversation_id FROM participants " +
                "LEFT JOIN deleted_conversations on participants.conversation_id=deleted_conversations.conversation_id " +
                "WHERE participants.user_id=" + id + " AND (deleted_conversations.user_id !=" + id + " OR deleted_conversations.user_id IS NULL)";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
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
                            msg.setCreated_at(rst.getDate(3));
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

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;
            if (participants == null || participants.size() == 0)
            {
                System.out.println("Cannot make new conversation without users");
                return false;
            }
            for (int i : participants) {

                Boolean exist = false;

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

    private static UserDTO getUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setId(rs.getInt(1));
        user.setEmail(rs.getString(2));
        user.setLogin(rs.getString(3));
        user.setFirstName(rs.getString(4));
        user.setLastName(rs.getString(5));
        user.setSex(rs.getString(6));
        user.setCreated_at(rs.getDate(7));
        user.setStatus(rs.getString(8));
        user.setAvatar_url(rs.getString(9));
        return user;
    }

    private static MessageDTO getMessage(ResultSet rs) throws SQLException {
        MessageDTO msg = new MessageDTO();
        msg.setId(rs.getInt(1));
        msg.setConversation_id(rs.getInt(2));
        msg.setFrom_id(rs.getInt(3));
        msg.setMessage(rs.getString(4));
        msg.setCreated_at(rs.getDate(5));
        msg.setAttachment_url(rs.getString(6));
        return msg;
    }

    public Integer addImage(String url) {
        Integer id = 0;
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            String SqlQuery;
            SqlQuery = "SELECT id FROM photos WHERE url='" + url + "';";
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

}
