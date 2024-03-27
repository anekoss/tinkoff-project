package edu.java.repository;

import edu.java.domain.TgChat;
import java.util.List;
import java.util.Optional;

public interface TgChatRepository {

    int save(TgChat tgChat);

    int delete(TgChat tgChat);

    List<TgChat> findAll();

    Optional<TgChat> findByChatId(Long chatId);

}
