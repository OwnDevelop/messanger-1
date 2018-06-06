package ru.dev.messanger.entities;

import ru.dev.messanger.BLL.BLL;

import java.time.Instant;
import java.util.UUID;

public class Token {

    private final int LIFETIME = 300;

    private final String STRING_VALUE = UUID.randomUUID().toString();
    private Instant expires;

    public Token() {
        this.expires = Instant.now().plusSeconds(LIFETIME);
    }

    public String getToken() {
        System.out.println(expires.compareTo(Instant.now()));
        if (expires.compareTo(Instant.now()) >= 0) {
            this.expires = Instant.now().plusSeconds(LIFETIME);
            return STRING_VALUE;
        } else {
            BLL.INSTANCE.removeToken(this);
            return "";
        }
    }

}
