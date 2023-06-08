package Commands;

import Encrypting.TestFormula;
import Entity.NoteEntity;

import Tools.Tools;
import Tools.CheckingForUpdate;

import java.util.List;
import java.util.Scanner;

public class Add extends Commands {

    /***

     -add- [название] [login] [password]

     ***/

    private final String postfix;

    public Add(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public String perform() throws UnknownArgsException {

        String[] args = Tools.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(args.length > 3) // Проверка на количество параметров в команде
            throw new UnknownArgsException("Параметров больше чем нужно");
        if(args.length < 3) // Проверка на количество параметров в команде
            throw  new UnknownArgsException("Вы забыли указать логин или пароль");

        if(args[2].contains("."))
            throw new UnknownArgsException("В пароле не должен содержаться символ '.'");

        return addNewNote(args);
    }

    //TODO Текущая конструкция аргументов команды под сомнением

    private String addNewNote(String[] args) {

        Scanner confirm = new Scanner(System.in);

        for(NoteEntity note: TestingClass.notes) {

            if(note.getIdService().toLowerCase().contains(args[0].toLowerCase())) {
                if(note.getIdService().contains("account")) {

                    String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 "аккаунтов" максимум

                    List<NoteEntity> allAccountsOfService = Tools.getAllAccounts(args[0]);
                    System.out.println("У данного сервиса уже есть " + allAccountsOfService.size() + " аккаунта(ов), " +
                            "добавить " + (allAccountsOfService.size()+1) + " ? (y/n)");

                    if(confirm.nextLine().equalsIgnoreCase("y")) {
                        // Не нужно +1, так как (например vk.com - allAccountsOfService.size() = 3) в массиве отсчёт начинается с нуля,
                        // то есть он и будет указывать на нужный новый номер аакаунта
                        String accountName = args[0] + " " + numberOfAccount[allAccountsOfService.size()] + " account"; // Добавление номера аккаунта
                        String login = args[1];
                        String password = TestFormula.analysisString(args[2]); // Шифрование пароля

                        NoteEntity newNote = new NoteEntity(accountName, login, password);
                        TestingClass.notes.add(newNote);

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return "Вы добавили новый аккаунт к сервису " + args[0] + " с такими данными:\n" + accountName + "\nLogin: " + login + "\nPassword: " + args[2];
                    }
                    else {
                        return "Аккаунт не был добавлен";
                    }
                }
                else {
                    System.out.println("Данный сервис уже существует, создать второй аккаунт ? (y/n)\n" + args[0] + "\nLogin: " + args[1] + "\nPassword: " + args[2]);
                    if(confirm.nextLine().equalsIgnoreCase("y")) {

                        note.setIdService( note.getIdService() + " 1-st account" );

                        NoteEntity newNote = new NoteEntity( args[0] + " 2-nd account", args[1], TestFormula.analysisString(args[2]) );
                        TestingClass.notes.add(newNote);

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return "Вы добавили новый аккаунт к сервису " + args[0] + " с такими данными:\n"
                                + newNote.getIdService() + "\nLogin: " + newNote.getLogin() + "\nPassword: " + args[2];
                    }
                    else {
                        return "Аккаунт НЕ добавлен";
                    }

                }
            }

        }

        System.out.println("Вы уверены что хотите добавить такую запись? (y/n)\n" + args[0] + "\nLogin: " + args[1] + "\nPassword: " + args[2]);

        if(confirm.nextLine().equalsIgnoreCase("y")) {
            String encrptPass = TestFormula.analysisString(args[2]); // Шифрование пароля

            NoteEntity newNote = new NoteEntity(args[0], args[1], encrptPass);
            TestingClass.notes.add(newNote);

            CheckingForUpdate.isUpdated = true;

            return "Запись добавлена успешно";
        } else
            return "Запись не добавлена";

    }

}
