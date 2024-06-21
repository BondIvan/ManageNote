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

        if(args.length == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        // [serviceName] + [login]
        if(args.length > 1) {
            return printNotes( getNote(args[0], args[1]) );
        }

        // [serviceName]
        return printNotes( getNote(args[0], null) );
    }

    public List<NoteEntity> getNote(String serviceName, String serviceLogin) throws AccessNotFoundException {

        List<NoteEntity> accounts = UsefulMethods.getAllAccountsForOneService(listWithNotes, serviceName);

        if(accounts.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.getOneBestMatch(serviceName, Dictionaries.uniqueServiceNames);
            System.out.println( AutoCorrectionServiceName.getThreeBestMatch(serviceName, Dictionaries.uniqueServiceNames) );

            throw new AccessNotFoundException("Сервис не найден.\n" + "Возможно вы имели в виду: " + possibleVariant);
        }

        if(serviceLogin == null || serviceLogin.isEmpty())
            return UsefulMethods.sortNoteEntityByServiceName(accounts);

        return List.of( UsefulMethods.getAccountFromServiceByLogin(accounts, serviceName, serviceLogin) );
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
