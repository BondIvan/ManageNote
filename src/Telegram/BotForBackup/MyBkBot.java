package Telegram.BotForBackup;

import Commands.WithParameters.Get;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import Telegram.Commands.SendMsg;
import Tools.UsefulMethods;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MyBkBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        if(!update.hasMessage() || !update.getMessage().hasText())
            return;

        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        String prefix = message.split(" ")[0]; // Введённая команда
        String postfix = message.substring(prefix.length()).trim(); // Аргументы введённой команды

        if(!prefix.equalsIgnoreCase("get"))
            return;

        try {

            for(NoteEntity nt: get(postfix, chatId))
                execute( SendMsg.sendMessage(chatId, nt.toString()) );

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public List<NoteEntity> get(String postfix, long chatId) throws TelegramApiException {

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        try {
            Get get = new Get();
            if(args.length > 1) {
                return List.of( get.getNoteByLogin(args[0], args[1]) );
            }
            return get.getListWithNotes(args[0]);
        } catch (AccessNotFoundException e) {
            System.out.println("Ошибка get в bkBot: " + e.getMessage());

            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), e.getMessage());
            execute(sendMessage);
        }

        throw new TelegramApiException("Странные данные");
    }

    @Override
    public String getBotUsername() {
        return Config.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

}
