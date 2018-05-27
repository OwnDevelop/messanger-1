package ru.dev.messanger.entities;

public class NewUserDTO extends UserDTO {
    private String password;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
