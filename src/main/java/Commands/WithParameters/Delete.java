package Commands.WithParameters;

import Commands.Commands;
import Encrypting.Security.Encryption_AES.AES_GCM;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.List;
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
            return deleteNoteByLogin(args[0], args[1]);
        }

        // [serviceName]
        return deleteNote(args[0]);
    }

    public String deleteNote(String serviceName) throws AccessNotFoundException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, serviceName); // Содержит необходимы-й/е аккаунт-/ы

        if(searchedServices.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(serviceName, Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        // [serviceName] + [login]
        if(searchedServices.size() > 1) {
            System.out.println("Укажите в команде логин аккаунта, который необходимо удалить");

            // Отсортировать все аккаунты сервиса по названию + вывести их названия и логины
            UsefulMethods.sortNoteEntityByServiceName( listWithNotes.stream()
                    .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase(serviceName))
                    .collect(Collectors.toList()) ).forEach((note) -> System.out.println(note.getIdService() + " -> " + note.getLogin()));

            return "Теперь введите команду";
        }

        // [serviceName]
        listWithNotes.remove(searchedServices.get(0));
        UsefulMethods.changingNameWhenRemove(listWithNotes, serviceName);

        CheckingForUpdate.isUpdated = true;

        try {
            AES_GCM.deleteKeyFromStorage(serviceName);
        } catch (Exception e) {
            return "Не удалось удалить, тип ошибки - " + e.getMessage();
        }

        return "Удалено";
    }

    public String deleteNoteByLogin(String serviceName, String serviceLogin) throws AccessNotFoundException {

        NoteEntity deletedNote = UsefulMethods.getAccountFromServiceByLogin(listWithNotes, serviceName, serviceLogin);

        // [serviceName]
        listWithNotes.remove(deletedNote);
        UsefulMethods.changingNameWhenRemove(listWithNotes, serviceName);

        CheckingForUpdate.isUpdated = true;

        try {
            AES_GCM.deleteKeyFromStorage(serviceName);
        } catch (Exception e) {
            return "Не удалось удалить, тип ошибки - " + e.getMessage();
        }

        return "Удалено";
    }
}
