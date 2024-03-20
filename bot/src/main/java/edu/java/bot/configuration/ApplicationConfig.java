package edu.java.bot.configuration;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandManager;
import edu.java.bot.printer.HtmlPrinter;
import edu.java.bot.printer.Printer;
import jakarta.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
public record ApplicationConfig(@NotEmpty String telegramToken) {
    private static final int THREAD_COUNT = 8;

    @Bean
    public Map<Long, Command> prevCommands() {
        return new HashMap<>();
    }

    @Bean
    public CommandManager commandManager(Command[] command, Command unknownCommand) {
        Map<String, Command> commandMap = new HashMap<>();
        Arrays.stream(command).forEach(command1 -> commandMap.put(command1.command(), command1));
        return new CommandManager(commandMap, unknownCommand);
    }

    @Bean
    public SetMyCommands setMyCommandAs(Command[] commands, Command unknownCommand) {
        BotCommand[] botCommands = Arrays.stream(commands).filter(command -> !command.equals(unknownCommand))
                                         .map(Command::toApiCommand).toArray(BotCommand[]::new);
        return new SetMyCommands(botCommands);
    }

    @Bean
    public Printer printer() {
        return new HtmlPrinter();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(THREAD_COUNT);
    }

}
