package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;

import java.util.List;

public class Get implements Commands {

    private final List<NoteEntity> listWithNotes;

    public Get() {
        this.listWithNotes = StartConsole.NOTES;
    }

    public Get(List<NoteEntity> notes) {
        this.listWithNotes = notes;
    }

    @Override
    public String perform(String postfix) throws UnknownArgsException, AccessNotFoundException {

        String[] args = UsefulMethods.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        //TODO [serviceName ][login]

        if(args.length == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        if(args.length > 1)
            return getNoteByLogin(args).toString();

        StringBuilder stringBuilder = new StringBuilder();
        getListWithNotes(args).forEach(note -> stringBuilder.append(note).append("\n"));

        return stringBuilder.toString();
    }

    public List<NoteEntity> getListWithNotes(String[] args) throws AccessNotFoundException {

        List<NoteEntity> accounts = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]);

        if( accounts.isEmpty() ) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        return accounts;
    }

    public NoteEntity getNoteByLogin(String[] args) throws AccessNotFoundException {

        return UsefulMethods.getAccountFromServiceByLogin(listWithNotes, args[0], args[1]);
    }

}
