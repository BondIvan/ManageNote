import Commands.*;

import Entity.NoteEntity;

import Tools.CheckingForUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final static String forTesting = "ForTesting.txt";
    private static final String access = "Access.txt";
    private static final String path = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\" + forTesting;

    public static List<NoteEntity> notes; //  Список всех доступов взятых из файла в виде объекта сущности NoteEntity

    public static void main(String[] args) throws Exception {


        console();
    }

    public static void console() throws Exception {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            } else if (input.equals("exit")) {

                if (CheckingForUpdate.isUpdated) {
                    Commands save = new Save(path);
                    save.perform();
                }

                scanner.close();
                System.exit(0);
            }

            String prefix = input.split(" ")[0]; // Введённая команда
            String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды

            if (Commands.isExist(prefix)) {

                try {
                    switch (prefix) {

                        case "get" -> {
                            Commands get = new Get(postfix);
                            System.out.println(get.perform());
                        }
                        case "add" -> {
                            Commands add = new Add(postfix);
                            System.out.println(add.perform());
                        }
                        case "replace" -> {
                            Commands replace = new Replace(postfix);
                            System.out.println(replace.perform());
                        }
                        case "delete" -> {
                            Commands delete = new Delete(postfix);
                            System.out.println(delete.perform());
                        }
                        case "help" -> {
                            Commands help = new Help();
                            System.out.println(help.perform());
                        }
                        case "save" -> {
                            Commands save = new Save(path);
                            System.out.println(save.perform());
                        }
                        case "getall" -> {
                            Commands getall = new GetAll();
                            System.out.println(getall.perform());
                        }

                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                System.err.println("Такой команды нет");
            }

        }

    }

}
