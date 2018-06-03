package ru.dev.messanger.entities;

import ru.dev.messanger.BLL.BLL;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class Token {

    private final int LIFETIME = 1;

    private final String STRING_VALUE = UUID.randomUUID().toString();
    private LocalDateTime expires;

    public Token() {
        this.expires = LocalDateTime.now().plusMinutes(LIFETIME);
    }

    public String getToken() {
        System.out.println(expires.getLong(ChronoField.NANO_OF_SECOND));
        System.out.println(LocalDateTime.now().getLong(ChronoField.NANO_OF_SECOND));
        if (expires.getLong(ChronoField.NANO_OF_SECOND) <=
                LocalDateTime.now().getLong(ChronoField.NANO_OF_SECOND)) {
            this.expires = LocalDateTime.now().plusMinutes(LIFETIME);
            return STRING_VALUE;
        } else {
            BLL.INSTANCE.removeToken(this);
            return "";
        }
    }

}