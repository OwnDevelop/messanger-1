package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

public class NewUserDTO extends UserDTO {
    @SerializedName("password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
