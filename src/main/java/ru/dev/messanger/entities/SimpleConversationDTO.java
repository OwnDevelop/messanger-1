package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SimpleConversationDTO {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("created_at")
    private Date created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
