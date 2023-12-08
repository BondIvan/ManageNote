package Commands.WithoutParameters;

import Commands.Commands;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.UsefulMethods;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class Push implements Commands {

    private final String pathToFile;

    public Push() {
        this.pathToFile = StartConsole.PATH;
    }

    @Override
    public String perform(String postfix) throws IOException, UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");
        if(!args[0].equals("bot"))
            throw new UnknownArgsException("Неверный параметр");

        return pushToBot();
    }

    private String pushToBot() throws IOException, IncorrectValueException {

        // sendFile(pathToFile);

        //TODO Файл отправляется от лица бота, нужно, чтобы файл был отправлен от моего лица

        return "Файл отправлен";
    }

    public void sendFile(String filePath) throws IOException, IncorrectValueException {

        HttpClient httpClient = HttpClients.createDefault();

        File fileSender = new File(filePath);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        long chatId = 347329462;
        builder.addTextBody("chat_id", String.valueOf(chatId));
        builder.addPart("document", new FileBody(fileSender, ContentType.DEFAULT_TEXT));

        String bot_token = "6223631615:AAFX66jboE6jDmL9m6fhY9pknTYBP4fMIxA";
        String API_BASE_URL = "https://api.telegram.org/bot" + bot_token + "/";
        HttpPost httpPost = new HttpPost(API_BASE_URL + "sendDocument");
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);

        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("File successfully send!");
        } else {
            throw new IncorrectValueException("Error sending the file. Status code: " + response.getStatusLine().getStatusCode());
        }
    }

}
