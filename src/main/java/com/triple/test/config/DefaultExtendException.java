package com.triple.test.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class DefaultExtendException extends Exception {

    @Getter
    private HttpStatus status;

    public DefaultExtendException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public DefaultExtendException(HttpStatus status, Throwable t) {
        super(message(t));
        this.status = status;
    }


    public static String message(Throwable t) {
        if (Optional.ofNullable(t).isPresent()) {
            while ((t != null) ) {
                t = t.getCause();
            }

            if (Optional.ofNullable(t).isPresent()){
                return t.getMessage();
            }
            return "";

        } else {
            return "";
        }
    }
}
