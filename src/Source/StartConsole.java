package Source;

import Commands.CommandFactory;
import Commands.Commands;
import Commands.WithOrWithoutParameters.GetAll;
import Commands.WithParameters.*;
import Commands.WithoutParameters.*;
import Entity.NoteEntity;
import Telegram.Bot.BotWithBackups;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;
import org.telegram.telegrambots.meta.ApiConstants;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

public class StartConsole {

    /***

     При запуске считывает файл с доступами в список. На протяжении выполнения программы, все записи хранятся в этом списке.
     По окончанию выполнения программы, файл перезапишется c информацией, которую содержит список.

     Всё редактирование доступов происходит в списке.

     ***/

    public static final String PATH = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\" +
            "ForTesting.txt"; // Тестовый файл
            //"Access.txt"; // Рабочий файл

    public static List<NoteEntity> NOTES = new ArrayList<>(); // Список всех записей (сервисов)

    public static void main(String[] args) throws Exception {

        NOTES = UsefulMethods.getAllNoteFromFile(PATH);

        String[] selectedFile = PATH.split("\\\\");
        System.out.println("Запущено с файлом: " + selectedFile[selectedFile.length-1]);

        Dictionaries dictionaries = new Dictionaries();
        dictionaries.fillingDictionaries(NOTES);

        CommandFactory factory = new CommandFactory();
        factory.registerCommand("add", Add.class);
        factory.registerCommand("delete", Delete.class);
        factory.registerCommand("get", Get.class);
        factory.registerCommand("replace", Replace.class);

        factory.registerCommand("copyfile", CopyFile.class);
        factory.registerCommand("exit", Exit.class);
        factory.registerCommand("getall", GetAll.class);
        factory.registerCommand("help", Help.class);
        factory.registerCommand("save", Save.class);
        factory.registerCommand("pushfile", PushFile.class);

        BotWithBackups bot = new BotWithBackups();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);

        Scanner inputLine = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String input = inputLine.nextLine().trim(); // Введённая строка

            if (input.isEmpty())
                continue;

            String prefix = input.split(" ")[0]; // Введённая команда
            String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды

            try {
                Commands command = factory.getCommand(prefix);
                System.out.println(command.perform(postfix));
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

        }

    }

}
