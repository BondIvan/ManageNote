package Telegram.Commands;

import Source.StartConsole;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

public class SendFile {

    public SendDocument sendFile(long chatId) {
        InputFile inputFile = new InputFile();

        File file = new File(StartConsole.PATH);
        inputFile.setMedia(file);

        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(inputFile.setMedia(file));
        sendDocument.setChatId(chatId);
        sendDocument.getProtectContent();

        return sendDocument;
    }

}
