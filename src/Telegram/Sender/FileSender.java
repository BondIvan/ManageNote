package Telegram.Sender;

import Telegram.Bot.Config;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class FileSender {

    private static final String API_BASE_URL = "https://api.telegram.org/bot" + Config.BOT_TOKEN + "/";

    public void sendFile(long chatID, String filePath) throws IOException {

        org.apache.http.client.HttpClient httpClient = HttpClients.createDefault();

        File fileSender = new File(filePath);

        HttpPost httpPost = new HttpPost(API_BASE_URL + "sendDocument");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("chat_id", String.valueOf(chatID));
        builder.addPart("document", new FileBody(fileSender, ContentType.DEFAULT_TEXT));

        HttpEntity httpEntity = builder.build();

        httpPost.setEntity(httpEntity);

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("File successfully sent!");
        } else {
            System.out.println("Error sending the file. Status code: " + response.getStatusLine().getStatusCode());
        }
    }

}
