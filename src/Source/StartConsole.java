package Source;

import Commands.*;
import Entity.NoteEntity;
import Tools.UsefulMethods;

import java.util.*;

public class StartConsole {

    public static final String PATH_TEST = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\ForTesting.txt";
    public static final String PATH_ACCESS = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\Access.txt";

    public static List<NoteEntity> NOTES = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        NOTES = UsefulMethods.getAllNoteFromFile(PATH_TEST);

        //TODO Некоторые методы perform вызывают exception, значит нужно каждый метод perform вызывать через try-catch

        Map<String, Commands> map = new HashMap<>();
        map.put("get", new Get(NOTES));
        map.put("getall", new GetAll(NOTES));
        map.put("add", new Add(NOTES));
        map.put("delete", new Delete(NOTES));
        map.put("replace", new Replace(NOTES));
        map.put("save", new Save(PATH_TEST, NOTES));
        map.put("help", new Help());
        map.put("copyfile", new CopyFile(PATH_TEST));
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
                        System.out.println(map.get(prefix).perform(postfix));
                    } else {
                        System.out.println(map.get(prefix).perform());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }

    }

}
