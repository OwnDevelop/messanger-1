package ru.dev.messanger.entities;

import java.time.Instant;
import java.util.UUID;

public class Token {

    public final static int LIFETIME = 300;

    private final String STRING_VALUE = UUID.randomUUID().toString();

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public Instant getExpires() {
        return expires;
    }

    private Instant expires;

    public Token() {
        this.expires = Instant.now().plusSeconds(LIFETIME);
    }

    public String getStringToken() {
        return STRING_VALUE;
    }

}
