package edu.java.service;

import edu.java.controller.exception.AlreadyRegisterException;
import edu.java.controller.exception.ChatNotFoundException;

public interface TgChatService {
    void register(long tgChatId) throws AlreadyRegisterException;

    void unregister(long tgChatId) throws ChatNotFoundException;
}
