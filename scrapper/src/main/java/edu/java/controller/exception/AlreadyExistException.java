package edu.java.controller.exception;

public class AlreadyExistException extends Exception {

    @Override
    public String getMessage() {
        return "Данная ссылка уже отслеживается";
    }
}
