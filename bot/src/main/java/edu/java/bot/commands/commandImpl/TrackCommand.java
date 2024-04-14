package edu.java.bot.commands.commandImpl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.exception.CustomServerErrorException;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandExecutionStatus;
import edu.java.bot.printer.Printer;
import edu.java.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final CommandService commandService;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
    }

    @Override
    public String handle(Update update, Printer printer) throws CustomServerErrorException {
        Long id = update.message().from().id();
        if (supports(update)) {
            return "Введите URL-ссылку, чтобы отслеживать обновления.";
        }
        String url = GET_TEXT_REQUEST.apply(update.message().text());
        CommandExecutionStatus result = commandService.track(id, url);
        return result.getMessage();
    }

}
