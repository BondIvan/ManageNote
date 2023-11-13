package Telegram.Bot;

import Commands.WithParameters.Get;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotWithBackups extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        //TODO Нужно будет как-то сделать уведомление о том, что файл успешно отправлен

        if(!update.hasMessage() || !update.getMessage().hasText())
            return;

        String[] message = update.getMessage().getText().split(" ");
        long chatId = update.getMessage().getChatId();

        String prefix = message[0];
        String postfix = message[1];

        if(prefix.equals("get")) {

            Get get = new Get();
            try {
                sendMessage(chatId, get.perform(postfix));
            } catch (UnknownArgsException | AccessNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void sendMessage(long chatId, String text) {

        String idChat = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(idChat, text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }

    // Идентификационный номер чата с ботом
    public long getChatID() {
        return 347329462;
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
