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

        if(postfix.isEmpty())
            throw  new UnknownArgsException("Нет параметров");

        // Разбитие postfix-а на состовляющие (конкретные аргументы команды)
        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        // [serviceName] + [serviceLogin]
        if(args.length > 1) {
            return deleteNote(args[0], args[1]);
        }

        // [serviceName]
        return deleteNote(args[0], null);
    }

    public String deleteNote(String serviceName, String serviceLogin) throws AccessNotFoundException {

        List<NoteEntity> accounts = UsefulMethods.getAllAccountsForOneService(listWithNotes, serviceName);

        if(accounts.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.getOneBestMatch(serviceName, Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            System.out.println( AutoCorrectionServiceName.getThreeBestMatch(serviceName, Dictionaries.uniqueServiceNames) );

            throw new AccessNotFoundException("Сервис не найден");
        }

        boolean existLogin = serviceLogin != null && !serviceLogin.isEmpty();
        if(accounts.size() > 1 && !existLogin) {
            System.out.println("Укажите в команде логин аккаунта, который необходимо удалить");
            // Отсортировать все аккаунты сервиса по названию + вывести их названия и логины
            UsefulMethods.sortNoteEntityByServiceName(listWithNotes.stream()
                    .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(serviceName))
                    .collect(Collectors.toList())).forEach((note) -> System.out.println(note.getServiceName() + " -> " + note.getLogin()));

            return "Теперь введите команду";
        }

        // [serviceName] : [serviceLogin] [serviceName]
        NoteEntity deletedNote = existLogin ?
                UsefulMethods.getAccountFromServiceByLogin(accounts, serviceName, serviceLogin) : accounts.get(0);
        try {

            String serviceID = deletedNote.getId();
            AES_GCM.deleteKeyFromStorage(serviceID);

            listWithNotes.remove(deletedNote);
            UsefulMethods.changingNameWhenRemove(listWithNotes, serviceName);

            CheckingForUpdate.isUpdated = true;

        } catch (Exception e) {
            return "Не удалось удалить, тип ошибки - " + e.getMessage();
        }

        return "Удалено";
    }

}
