package Commands;

import Entity.NoteEntity;
import OptionsExceptions.UnknownArgsException;
import OptionsExceptions.WrongPostfixMethodException;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.List;
import java.util.Scanner;

public class Add extends Commands {

    private final List<NoteEntity> listWithNotes;

    public Add(List<NoteEntity> listWithNotes) {
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws Exception {
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

        if( addNewNote(args) ) {
            CheckingForUpdate.isUpdated = true;
            return "Сервис добавлен";
        } else
            return "Сервис НЕ добавлен";
    }

    private boolean addNewNote(String[] args) throws UnknownArgsException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        String[] numberOfAccount = {"1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th"}; // 10 "аккаунтов" максимум

        System.out.println( "Добавить " + numberOfAccount[searchedServices.size()] + " сервис с такими данными ? (y/n):\n"
                + args[0] +
                "\nLogin: " + args[1] +
                "\nPassword: " + args[2] );

        Scanner confirm = new Scanner(System.in);
        if( !confirm.nextLine().equals("y") )
            return false;

        if(searchedServices.size() == 1) {
            int positionOfFindNoteInMainList = listWithNotes.indexOf(searchedServices.get(0)); // Позиция требуемого сервиса в списке (главного), который был считан с файла
            NoteEntity currentNoteInMainList = listWithNotes.get(positionOfFindNoteInMainList); // Сервис непосредственно взят из (главного) списка

            currentNoteInMainList.setIdService(currentNoteInMainList.getIdService() + " 1-st account"); // Добавление порядкового номера к существующему сервису
        }

        String serviceId = searchedServices.size() >= 1 ?
                args[0] + " " + numberOfAccount[searchedServices.size()] + " account"
                :args[0];

        NoteEntity newNote = new NoteEntity( // Добавляемый сервис/аккаунт
                serviceId,
                args[1] );
        newNote.setPassword(args[2]);

        listWithNotes.add(newNote);

        return true;
    }
}
