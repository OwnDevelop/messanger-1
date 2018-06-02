package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;
import ru.dev.messanger.BLL.BLL;

public class TUser extends NewUserDTO {
    @SerializedName("token")
    private String token;

    public TUser(UserDTO user) {
        setId(user.getId());
        setLogin(user.getLogin());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setSex(user.getSex());
        setStatus(user.getStatus());
        setCreated_at(user.getCreated_at());
        setAvatar_url(user.getAvatar_url());

        Token token = new Token();
        BLL.INSTANCE.addTokenToUser(this, token);

        this.token = token.getToken();
    }

    public String getToken() {
        return this.token;
    }
}
