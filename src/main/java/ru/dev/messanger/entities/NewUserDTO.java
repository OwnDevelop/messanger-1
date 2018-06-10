package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotBlank;

public class NewUserDTO extends UserDTO {

    @NotBlank(message = "Password cannot be empty")
    @SerializedName("password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
