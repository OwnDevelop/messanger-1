package ru.dev.messanger.entities;

import ru.dev.messanger.BLL.BLL;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class Token {

    private final int LIFETIME = 60;

    private final String STRING_VALUE = UUID.randomUUID().toString();
    private Instant expires;

    public Token() {
        this.expires = Instant.now().plusSeconds(LIFETIME);
    }

    public String getToken() {
        System.out.println(expires.getLong(ChronoField.NANO_OF_SECOND));
        System.out.println(LocalDateTime.now().getLong(ChronoField.NANO_OF_SECOND));
        if (expires.getLong(ChronoField.NANO_OF_SECOND) <=
                LocalDateTime.now().getLong(ChronoField.NANO_OF_SECOND)) {
            this.expires = Instant.now().plusSeconds(LIFETIME);
            return STRING_VALUE;
        } else {
            BLL.INSTANCE.removeToken(this);
            return "";
        }
    }

}
