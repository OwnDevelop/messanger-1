package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;
import ru.dev.messanger.BLL.BLL;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class UserDTO {

    @SerializedName("id")
    private int id;
    @SerializedName("login")
    private String login;
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("sex")
    private String sex;
    @SerializedName("status")
    private String status;
    @SerializedName("created_at")
    private Instant created_at;
    @SerializedName("avatar_url")
    private String avatar_url;
    @SerializedName("activation_code")
    private String activation_code;

    public String getActivation_code() { return activation_code; }

    public void setActivation_code(String activation_code) { this.activation_code = activation_code; }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName(){return firstName;}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreated_at() { return created_at; }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }


}
