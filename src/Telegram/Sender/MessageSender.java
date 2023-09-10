package Telegram.Sender;

import Telegram.Bot.Config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MessageSender {

    private static final String API_BASE_URL = "https://api.telegram.org/bot" + Config.BOT_TOKEN + "/";

    public static String sendMessage(long chatID, String message) throws URISyntaxException, IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = new URI( API_BASE_URL + "sendMessage?chat_id=" + chatID + "&text=" + message );

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            return "Сообщение успешно отправлено!";
        } else {
            return "Ошибка при отправке сообщения. Код ошибки: " + response.statusCode();
        }
    }

}
