package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.List;
import java.util.Scanner;

public class Add implements Commands {

    private final List<NoteEntity> listWithNotes;

    public Add() {
        this.listWithNotes = StartConsole.NOTES;
    }

    public Add(List<NoteEntity> notes) {
        this.listWithNotes = notes;
    }

    @Override
    public String perform(String postfix) throws UnknownArgsException, IncorrectValueException {

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

    private boolean addNewNote(String[] args) throws UnknownArgsException, IncorrectValueException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        String[] numberOfAccount = {"1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th"}; // 10 "аккаунтов" максимум

        System.out.println( "Добавить " + numberOfAccount[searchedServices.size()] + " сервис с такими данными ? (y/n):\n"
                + args[0] +
                "\nLogin: " + args[1] +
                "\nPassword: " + args[2] );

        Scanner confirm = new Scanner(System.in);
        if( !confirm.nextLine().equals("y") )
            return false;

        NoteEntity newNote = new NoteEntity( // Добавляемый сервис/аккаунт
                args[0], // Название
                args[1] ); // Логин
        newNote.setPassword(args[2]); // Пароль

        if( searchedServices.stream().anyMatch(note -> note.getLogin().equals(args[1])) )
            throw new IncorrectValueException("У этого сервиса такой логин уже существует");

        listWithNotes.add(newNote);
        UsefulMethods.changingNameWhenAdd(listWithNotes, args[0]);

        return true;
    }
}
