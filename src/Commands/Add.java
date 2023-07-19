package Commands;

import Encrypting.Alphabet.Alphabet;
import Encrypting.ViewEncrypt;
import Entity.NoteEntity;

import OptionsExceptions.UnknownArgsException;
import OptionsExceptions.WrongPostfixMethodException;
import Tools.UsefulMethods;
import Tools.CheckingForUpdate;

import java.util.List;
import java.util.Scanner;

public class Add extends Commands {

    /***

     -add- [название] [login] [password]

     ***/

    // Список, в который нужно добавить
    private final List<NoteEntity> listWithNotes;

    public Add(List<NoteEntity> listWithNotes) {
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws WrongPostfixMethodException {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

    @Override
    public String perform(String postfix) throws Exception {

        String[] args = UsefulMethods.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(args.length > 3) // Проверка на количество параметров в команде
            throw new UnknownArgsException("Параметров больше чем нужно");
        if(args.length < 3) // Проверка на количество параметров в команде
            throw  new UnknownArgsException("Вы забыли указать логин или пароль");
        if(args[2].contains(".")) // Символ '.' используется для шифрования и расшифрования
            throw new UnknownArgsException("В пароле не должен содержаться символ '.'");

        return addNewNote(args);
    }

    //TODO Текущая конструкция аргументов команды под сомнением

    private String addNewNote(String[] args) {

        Scanner confirm = new Scanner(System.in);

        ViewEncrypt viewEncrypt = new ViewEncrypt( Alphabet.getAlpha() ); // Объект для шифрования

        for(NoteEntity note: listWithNotes) {

            String currentServiceName = note.getIdService();
            if(currentServiceName.split(" ")[0].equalsIgnoreCase(args[0])) { // Сравнивается первое слово текущего сервиса с требуемым
                if(currentServiceName.contains("account")) { // Если у сервиса больше одного аккаунта

                    String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 "аккаунтов" максимум

                    List<NoteEntity> allAccountsOfService = UsefulMethods.getAllAccounts(listWithNotes, args[0]);
                    System.out.println("У данного сервиса уже есть " + allAccountsOfService.size() + " аккаунта(ов), " +
                            "добавить " + (allAccountsOfService.size()+1) + " ? (y/n)");

                    if(confirm.nextLine().equalsIgnoreCase("y")) {
                        // Не нужно +1, так как (например vk.com - allAccountsOfService.size() = 3) в массиве отсчёт начинается с нуля,
                        // то есть он и будет указывать на нужный новый номер аакаунта
                        String accountName = args[0] + " " + numberOfAccount[allAccountsOfService.size()] + " account"; // Добавление номера аккаунта
                        String login = args[1];
                        String password = viewEncrypt.encrypting(args[2]); // Шифрование пароля

                        NoteEntity newNote = new NoteEntity(accountName, login, password);
                        listWithNotes.add(newNote);

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return "Вы добавили новый аккаунт к сервису " + args[0] + " с такими данными:\n"
                                + accountName +
                                "\nLogin: " + login +
                                "\nPassword: " + args[2];
                    }
                    else {
                        return "Аккаунт не был добавлен";
                    }
                }
                else { // Если у сервиса всего 1 аккаунт
                    System.out.println("Данный сервис уже существует, создать второй аккаунт ? (y/n)\n" + args[0] + "\nLogin: " + args[1] + "\nPassword: " + args[2]);
                    if(confirm.nextLine().equalsIgnoreCase("y")) {

                        note.setIdService( currentServiceName + " 1-st account" );

                        String encryptedPassword = viewEncrypt.encrypting(args[2]);
                        NoteEntity newNote = new NoteEntity( args[0] + " 2-nd account", args[1], encryptedPassword);
                        listWithNotes.add(newNote);

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return "Вы добавили новый аккаунт к сервису " + args[0] + " с такими данными:\n"
                                + newNote.getIdService() +
                                "\nLogin: " + newNote.getLogin() +
                                "\nPassword: " + args[2];
                    }
                    else {
                        return "Аккаунт НЕ добавлен";
                    }

                }
            }

        }

        // Добавляемого сервиса вообще НЕ существует
        System.out.println("Вы уверены что хотите добавить такую запись? (y/n)\n" + args[0] + "\nLogin: " + args[1] + "\nPassword: " + args[2]);

        if(confirm.nextLine().equalsIgnoreCase("y")) {
            String encryptedPassword = viewEncrypt.encrypting(args[2]); // Шифрование пароля
            NoteEntity newNote = new NoteEntity(args[0], args[1], encryptedPassword);
            listWithNotes.add(newNote);

            CheckingForUpdate.isUpdated = true;

            return "Запись добавлена успешно";
        } else
            return "Запись не добавлена";

    }

}
