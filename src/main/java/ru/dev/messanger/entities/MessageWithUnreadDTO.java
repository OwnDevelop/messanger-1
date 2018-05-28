package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

public class MessageWithUnreadDTO extends MessageDTO {
    @SerializedName("unreaded")
    private Integer countUnread;

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }
}
