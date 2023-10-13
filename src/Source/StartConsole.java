package Source;

import Commands.*;
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

    public static final String PATH_TEST = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\ForTesting.txt";
    public static final String PATH_ACCESS = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\Access.txt";

    // Список всех записей (сервисов)
    public static List<NoteEntity> NOTES = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        NOTES = UsefulMethods.getAllNoteFromFile(PATH_TEST);

        Dictionaries dictionaries = new Dictionaries(NOTES);
        dictionaries.fillingDictionaries(); // Заполнение словаря для автокоррекции

        CommandFactoryWithParameters factoryWithParameters = new CommandFactoryWithParameters();
        factoryWithParameters.registerCommand("get", Get.class);
        factoryWithParameters.registerCommand("delete", Delete.class);
        factoryWithParameters.registerCommand("add", Add.class);
        factoryWithParameters.registerCommand("replace", Replace.class);

        CommandFactoryWithoutParameters factoryWithoutParameters = new CommandFactoryWithoutParameters();
        factoryWithoutParameters.registerCommand("copyfile", CopyFile.class);
        factoryWithoutParameters.registerCommand("exit", Exit.class);
        factoryWithoutParameters.registerCommand("getall", GetAll.class);
        factoryWithoutParameters.registerCommand("help", Help.class);
        factoryWithoutParameters.registerCommand("save", Save.class);

//        Map<String, CommandsWithParameters> map = new HashMap<>();
//        map.put("get", new Get(NOTES));
//        map.put("getall", new GetAll(NOTES));
//        map.put("add", new Add(NOTES));
//        map.put("delete", new Delete(NOTES));
//        map.put("replace", new Replace(NOTES));
//        map.put("save", new Save(PATH_TEST, NOTES));
//        map.put("copyfile", new CopyFile(PATH_TEST));
//        map.put("help", new Help());
//        map.put("exit", new Exit(PATH_TEST, NOTES));

        String[] commandWithPostfix = { "add", "get", "delete", "replace" };

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine().trim(); // Введённая строка

            if (input.isEmpty())
                continue;

            String prefix = input.split(" ")[0]; // Введённая команда
            String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды

//            if( map.containsKey(prefix) ) {
//
//                try {
//                    if (Arrays.asList(commandWithPostfix).contains(prefix)) {
//                        System.out.println(map.get(prefix).perform(postfix)); // С postfix
//                    } else {
//                        System.out.println(map.get(prefix).perform()); // Без postfix
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//
//            } else {
//                System.out.println("Такой команды нет");
//            }
        }

    }

}
