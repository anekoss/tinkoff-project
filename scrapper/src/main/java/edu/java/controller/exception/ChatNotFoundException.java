package edu.java.controller.exception;

public class ChatNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Чат c таким id не найден";
    }
}
