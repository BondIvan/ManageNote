package Telegram.Bot;

import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BotWithBackups extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        //TODO Сделать другую фабрику, которая будет отвечать чисто за команды для бота

        if(!update.hasMessage() || !update.getMessage().hasText())
            return;

        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        String prefix = message.split(" ")[0]; // Введённая команда
        String postfix = message.substring(prefix.length()).trim(); // Аргументы введённой команды

        BotGet botGet = new BotGet();

        try {

            //Commands command = commandFactory.getCommand(prefix);
            if(prefix.equals("get"))
                sendMessage(chatId, botGet.perform(postfix));

        } catch (AccessNotFoundException | UnknownArgsException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMessage(long chatId, String text) {

        int placeLastSpace = text.lastIndexOf(" ");
        int amongCharacterInPassword = text.length() - placeLastSpace;

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType("spoiler");
        messageEntity.setOffset(placeLastSpace); // С какого символа начинать скрывать
        messageEntity.setLength(amongCharacterInPassword); // На сколько символов скрывать

        List<MessageEntity> messageEntityList = new ArrayList<>();
        messageEntityList.add(messageEntity);

        String idChat = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(idChat, text);
        sendMessage.disableNotification();

        sendMessage.setEntities(messageEntityList);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }

    public void sendFile(long chatId) {

        InputFile inputFile = new InputFile();

        File file = new File(StartConsole.PATH);
        inputFile.setMedia(file);

        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(inputFile.setMedia(file));
        sendDocument.setChatId(chatId);
        sendDocument.getProtectContent();

        try {
            execute(sendDocument);
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
