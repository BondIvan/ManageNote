package Source;

import Commands.CommandFactory;
import Commands.Commands;
import Commands.WithParameters.*;
import Commands.WithoutParameters.*;
import Entity.NoteEntity;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;

import java.util.*;

public class StartConsole {

    //TODO Изменение названия сервиса, сервис с таким названием уже существует

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
