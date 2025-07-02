package com.project.api.rest.exception;

public class UserUnauthorizedException extends RuntimeException {
    public UserUnauthorizedException(String message) { super(message); }
}
