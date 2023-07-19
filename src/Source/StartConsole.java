package Source;

import Commands.Commands;
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
//        map.put("get", );
//        map.put("getall", );
//        map.put("add", );
//        map.put("delete", );
//        map.put("replace", );
//        map.put("save", );
//        map.put("help", );
//        map.put("copyfile", );

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine().trim(); // Введённая строка
            // get microsoft.com
            if (input.isEmpty())
                continue;

            String prefix = input.split(" ")[0]; // Введённая команда
            String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды


        }

    }

}
