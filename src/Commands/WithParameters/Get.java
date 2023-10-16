package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;

import java.util.List;

import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import OptionsExceptions.WrongPostfixMethodException;

import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;

public class Get implements Commands {

    public Get() {
    }

    private final List<NoteEntity> listWithNotes = StartConsole.NOTES;

    @Override
    public String perform(String postfix) throws Exception {

        String[] args = UsefulMethods.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(args.length == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 1)
            throw new UnknownArgsException("Параметров больше чем нужно");
        else
            return getNote(args).toString();
    }

    private NoteEntity getNote(String[] args) throws Exception {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]);

        if(searchedServices.isEmpty()) {

            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        NoteEntity findNote;
        if(searchedServices.size() == 1) {
            findNote = searchedServices.get(0);
        } else {
            findNote = UsefulMethods.getAccountFromServiceByLogin(searchedServices, args[0]);
        }

        return findNote;
    }
}
