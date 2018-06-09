package ru.dev.messanger.entities;

import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.Instant;

public class SentMessageDTO {
    @SerializedName("id")
    private int id;

    @PositiveOrZero
    @SerializedName("from_id")
    private int from_id;

    @PositiveOrZero
    @SerializedName("conversation_id")
    private int conversation_id;

    @NotBlank
    @Size(min = 0, max = 200, message = "Message must be between 0 and 200 characters")
    @SerializedName("message")
    private String message;

    @SerializedName("attachment_url")
    private String attachment_url;
    @SerializedName("created_at")
    private Instant created_at;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }
}
