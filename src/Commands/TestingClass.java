package Commands;

import Entity.NoteEntity;

import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestingClass {

    /***

     При запуске считывает файл с доступами в список. На протяжении выполнения программы, все записи хранятся в этом списке.
     По окончанию выполнения программы, файл перезапишется информацией, которую содержит список.

     Всё редактирование доступов происходит в списке.

     ***/

    public static List<NoteEntity> notes = new ArrayList<>(); //  Список всех доступов взятых из файла в виде объекта сущности NoteEntity

    private final static String testFile = "ForTesting.txt";
    private static final String workFile = "Access.txt";
    private static final String path = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\" + workFile;

    public static void main(String[] args) throws Exception {

        notes = UsefulMethods.getAllNoteFromFile(path); // Заполнение списка доступами

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                continue;

            String prefix = input.split(" ")[0]; // Введённая команда
            String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды

            if (Commands.isExist(prefix)) {

                try {
                    switch (prefix) {

                        case "get" -> {
                            Commands get = new Get(TestingClass.notes);
                            System.out.println(get.perform());
                        }
                        case "add" -> {
                            Commands add = new Add(TestingClass.notes);
                            System.out.println(add.perform());
                        }
                        case "replace" -> {
                            Commands replace = new Replace(TestingClass.notes);
                            System.out.println(replace.perform());
                        }
                        case "delete" -> {
                            Commands delete = new Delete(TestingClass.notes);
                            System.out.println(delete.perform());
                        }
                        case "help" -> {
                            Commands help = new Help();
                            System.out.println(help.perform());
                        }
                        case "save" -> {
                            Commands save = new Save(path, TestingClass.notes);
                            System.out.println(save.perform());
                        }
                        case "copyfile" -> {
                            Commands copyFile = new CopyFile(path);
                            System.out.println(copyFile.perform());
                        }
                        case "getall" -> {
                            Commands getall = new GetAll(TestingClass.notes);
                            System.out.println(getall.perform());
                        }
                        case "exit" -> {
                            if(CheckingForUpdate.isUpdated) {
                                Commands save = new Save(path, TestingClass.notes);
                                save.perform();
                            }

                            scanner.close();
                            System.exit(0);
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
