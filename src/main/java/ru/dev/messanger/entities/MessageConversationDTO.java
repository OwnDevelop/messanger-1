package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

public class MessageConversationDTO extends SentMessageDTO {
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("title")
    private String title;
    @SerializedName("countUnread")
    private Integer countUnread;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }
}
