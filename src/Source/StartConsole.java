package Source;

import Commands.*;
import Commands.WithParameters.Add;
import Commands.WithParameters.Delete;
import Commands.WithParameters.Get;
import Commands.WithParameters.Replace;
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

        Dictionaries dictionaries = new Dictionaries(NOTES);
        dictionaries.fillingDictionaries();

        Map<String, Commands> map = new HashMap<>();
        map.put("get", new Get());
        map.put("getall", new GetAll());
        map.put("add", new Add());
        map.put("delete", new Delete());
        map.put("replace", new Replace());
        map.put("save", new Save());
        map.put("copyfile", new CopyFile());
        map.put("help", new Help());
        map.put("exit", new Exit());

        String[] commandWithPostfix = { "add", "get", "delete", "replace" };

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine().trim(); // Введённая строка

            if (input.isEmpty())
                continue;

            String prefix = input.split(" ")[0]; // Введённая команда
            String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды

            if( map.containsKey(prefix) ) {

                try {
                    if (Arrays.asList(commandWithPostfix).contains(prefix)) {
                        System.out.println(map.get(prefix).perform(postfix)); // С postfix
                    } else {
                        System.out.println(map.get(prefix).perform()); // Без postfix
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {
                System.out.println("Такой команды нет");
            }
        }

    }

}
