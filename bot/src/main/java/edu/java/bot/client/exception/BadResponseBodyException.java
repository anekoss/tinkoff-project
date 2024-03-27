package edu.java.bot.client.exception;

public class BadResponseBodyException extends Exception {
    @Override
    public String getMessage() {
        return "Bad response body was returned from the service";
    }
}
