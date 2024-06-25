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

        if(postfix.isEmpty())
            throw  new UnknownArgsException("Нет параметров");

        // Разбитие postfix-а на состовляющие (конкретные аргументы команды)
        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        // [serviceName] + [serviceLogin]
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

            if(possibleVariant == null) {
                System.out.println("Ничего не найдено. Наиболее подходящие варианты:");
                System.out.println(AutoCorrectionServiceName.getAllBestMatch(serviceName, Dictionaries.uniqueServiceNames));
            }
            else
                System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
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
