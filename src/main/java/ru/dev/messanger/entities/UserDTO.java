package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;
import ru.dev.messanger.BLL.BLL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class UserDTO {

    @SerializedName("id")
    private int id;
    @NotBlank(message = "login cannot be empty")
    @SerializedName("login")
    private String login;
    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    @SerializedName("email")
    private String email;
    @NotBlank(message = "firstName cannot be empty")
    @SerializedName("firstName")
    private String firstName;
    @NotBlank(message = "lastName cannot be empty")
    @SerializedName("lastName")
    private String lastName;
    @NotBlank(message = "sex cannot be empty")
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

    public int getStatusInt() {
        switch (status){
            case "Online": return 1;
            case "Idle": return 2;
            case "Do Not Disturb": return 3;
            case "Offline": return 4;
        }
        return 1;
    }
    public void setStatusInt(int status) {
        this.status = String.valueOf(status);
    }

}
