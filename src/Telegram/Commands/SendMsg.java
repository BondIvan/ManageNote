package Telegram.Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class SendMsg {

    public static SendMessage sendMessage(long chatId, String text) {

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

        return sendMessage;
    }

}
