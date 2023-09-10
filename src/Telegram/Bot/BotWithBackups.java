package Telegram.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotWithBackups extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        //TODO Нужно будет как-то сделать уведомление о том, что файл успешно отправлен

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
