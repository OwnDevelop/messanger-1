package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MessageDTO extends SentMessageDTO {
    private int id;
    @SerializedName("avatar_url")
    private String image_url;
    @SerializedName("firstName")
    private String first_name;
    @SerializedName("lastName")
    private String last_name;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
