package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ConversationDTO extends SimpleConversationDTO{

    @SerializedName("admin_id")
    private int admin_id;

    @SerializedName("participants_id")
    private List<Integer> participants_id;

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public List<Integer> getParticipants_id() {
        return participants_id;
    }

    public void setParticipants_id(List<Integer> participants_id) {
        this.participants_id = participants_id;
    }
}
