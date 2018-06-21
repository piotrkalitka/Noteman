package com.piotrkalitka.noteman.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorisedException extends RuntimeException {

    public UnauthorisedException() {
        super("You are unauthorised to access this resource");
    }
}