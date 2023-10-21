package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.List;
import java.util.Scanner;

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
        if(args.length > 1)
            throw new UnknownArgsException("Параметров больше чем нужно");

        if( deleteNote(args) ) {
            CheckingForUpdate.isUpdated = true;
            return "Сервис удалён";
        } else
            return "Удаление НЕ произошло";
    }

    private boolean deleteNote(String[] args) throws AccessNotFoundException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        Scanner confirm = new Scanner(System.in);

        if(searchedServices.isEmpty()) {

            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        NoteEntity deletedNote;
        if(searchedServices.size() == 1) {
            deletedNote = searchedServices.get(0);
        } else {
            deletedNote = UsefulMethods.getAccountFromServiceByLogin(searchedServices, args[0]);
        }

        System.out.println("Будет удалён сервис: " + deletedNote.getIdService());

        System.out.println("Подтвердить? (y/n)");
        if( !confirm.nextLine().equals("y") ) {
            return false;
        }

        listWithNotes.remove(deletedNote);
        UsefulMethods.changingNameOfAccount(listWithNotes, args[0]);

        return true;
    }
}
