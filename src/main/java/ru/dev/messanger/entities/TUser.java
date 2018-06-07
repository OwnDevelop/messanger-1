package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;
import ru.dev.messanger.BLL.BLL;

public class TUser extends NewUserDTO {

    @SerializedName("token")
    private String token;

    public TUser(UserDTO user, String tkn) {
        setId(user.getId());
        setLogin(user.getLogin());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setSex(user.getSex());
        setStatus(user.getStatus());
        setCreated_at(user.getCreated_at());
        setAvatar_url(user.getAvatar_url());

        this.token = tkn;
    }

    public String getToken() {
        return this.token;
    }
}
