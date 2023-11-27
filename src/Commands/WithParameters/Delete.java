package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import javax.ws.rs.client.ClientRequestFilter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Delete implements Commands {

    private final List<NoteEntity> listWithNotes;

    public Delete() {
        this.listWithNotes = StartConsole.NOTES;
    }

    public Delete(List<NoteEntity> notes) {
        this.listWithNotes = notes;
    }

    @Override
    public String perform(String postfix) throws UnknownArgsException, AccessNotFoundException {

        String[] args = UsefulMethods.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(args.length == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        // [serviceName] + [login]
        if(args.length > 1) {
            return deleteNoteByLogin(args);
        }

        // [serviceName]
        return deleteNote(args);
    }

    public String deleteNote(String[] args) throws AccessNotFoundException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        if(searchedServices.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        // [serviceName] + [login]
        if(searchedServices.size() > 1) {
            System.out.println("Укажите в команде логин аккаунта, который необходимо удалить");

            // Отсортировать все аккаунты сервиса по названию + вывести их названия и логины
            UsefulMethods.sortNoteEntityByServiceName( listWithNotes.stream()
                    .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase(args[0]))
                    .collect(Collectors.toList()) ).forEach((note) -> System.out.println(note.getIdService() + " -> " + note.getLogin()));

            return "Теперь введите команду";
        }

        // [serviceName]
        listWithNotes.remove(searchedServices.get(0));
        UsefulMethods.changingNameWhenRemove(listWithNotes, args[0]);

        CheckingForUpdate.isUpdated = true;

        return "Удалено";
    }

    public String deleteNoteByLogin(String[] args) throws AccessNotFoundException {

        NoteEntity deletedNote = UsefulMethods.getAccountFromServiceByLogin(listWithNotes, args[0], args[1]);

        // [serviceName]
        listWithNotes.remove(deletedNote);
        UsefulMethods.changingNameWhenRemove(listWithNotes, args[0]);

        CheckingForUpdate.isUpdated = true;

        return "Удалено";
    }
}
