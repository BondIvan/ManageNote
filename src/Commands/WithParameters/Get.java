package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;

import java.util.ArrayList;
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

        if(args.length == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        // [serviceName] + [login]
        if(args.length > 1) {
            return printNotes( List.of(getNoteByLogin(args)) );
        }

        // [serviceName]
        return printNotes( getListWithNotes(args) );
    }

    public List<NoteEntity> getListWithNotes(String[] args) throws AccessNotFoundException {

        List<NoteEntity> accounts = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]);

        if( accounts.isEmpty() ) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        return UsefulMethods.sortNoteEntityByServiceName(accounts);
    }

    public NoteEntity getNoteByLogin(String[] args) throws AccessNotFoundException {

        List<NoteEntity> service = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]);

        if(service.isEmpty())
            throw new AccessNotFoundException("Сервис не найден");

        return UsefulMethods.getAccountFromServiceByLogin(listWithNotes, args[0], args[1]);
    }

    private String printNotes(List<NoteEntity> notes) {

        String start = "--------------------------------------\n";
        String end = "\n--------------------------------------";

        if(notes.size() < 2)
            return start + notes.get(0).toString() + end;

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < notes.size(); i++) {
            stringBuilder.append(start).append(notes.get(i).toString());

            if(i+1 != notes.size())
                stringBuilder.append("\n");
        }
        stringBuilder.append(end);

        return stringBuilder.toString();
    }

}
