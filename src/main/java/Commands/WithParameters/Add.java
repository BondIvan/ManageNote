package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.List;

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

        if( addNewNote(args[0], args[1], args[2]) ) {
            CheckingForUpdate.isUpdated = true;
            return "Сервис добавлен";
        } else
            return "Сервис НЕ добавлен";
    }

    private boolean addNewNote(String serviceName, String serviceLogin, String servicePassword) throws UnknownArgsException, IncorrectValueException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, serviceName); // Содержит необходимы-й/е аккаунт-/ы

        String[] numberOfAccount = {"1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th"}; // 10 "аккаунтов" максимум

        System.out.println( "Будет добавлен " + numberOfAccount[searchedServices.size()] + " сервис с такими данными:\n"
                + serviceName +
                "\nLogin: " + serviceLogin +
                "\nPassword: " + servicePassword );

        NoteEntity newNote = new NoteEntity( // Добавляемый сервис/аккаунт
                serviceName, // Название
                serviceLogin ); // Логин
        newNote.setPassword(servicePassword); // Пароль

        if( searchedServices.stream().anyMatch(note -> note.getLogin().equals(serviceLogin)) )
            throw new IncorrectValueException("У этого сервиса такой логин уже существует");

        listWithNotes.add(newNote);
        UsefulMethods.changingNameWhenAdd(listWithNotes, serviceName);

        return true;
    }
}
