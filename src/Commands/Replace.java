package Commands;

import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import OptionsExceptions.WrongPostfixMethodException;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.CheckingForUpdate;
import Tools.UsefulMethods;

import java.util.List;
import java.util.Scanner;

public class Replace extends Commands {

    private final List<NoteEntity> listWithNotes;

    public Replace(List<NoteEntity> listWithNotes) {
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

    @Override
    public String perform(String postfix) throws Exception {

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length < 3)
            throw new UnknownArgsException("Параметров меньше чем нужно");
        if(args.length > 3)
            throw new UnknownArgsException("Параметров больше чем нужно");

        if(replaceNote(args) == null) {
            return "Изменения не произошли";
        } else {
            CheckingForUpdate.isUpdated = true;
            return "Изменения приняты";
        }
    }

    private NoteEntity replaceNote(String[] args) throws Exception {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы
        Scanner confirm = new Scanner(System.in);

        if(searchedServices.isEmpty()) {

            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        NoteEntity replacedNote; // Этот сервис взят из списка notes
        // поэтому, если изменять его просто так, то он будет изменён только в списке notes, не в главном списке
        if(searchedServices.size() == 1) {
            replacedNote = searchedServices.get(0);
        } else {
            replacedNote = UsefulMethods.getAccountFromServiceByLogin(searchedServices, args[0]);
        }

        int positionOfFindNoteInMainList = listWithNotes.indexOf(replacedNote); // Позиция требуемого сервиса в списке (главного), который был считан с файла
        NoteEntity currentNoteInMainList = listWithNotes.get(positionOfFindNoteInMainList); // Сервис непосредственно взят из (главного) списка

        // Тернарный оператор с 3 условиями (вложенные)
        System.out.println( "Будут произведены следующие изменения в сервисе " + currentNoteInMainList.getIdService() + " с параметром " + args[1] + ": "
                + ( args[1].equals("service") ? currentNoteInMainList.getIdService()
                :args[1].equals("login") ? currentNoteInMainList.getLogin()
                :currentNoteInMainList.getPassword(true) )
                + " -> " + args[2] );

        System.out.println("Подтвердить ? (y/n)");
        if( !confirm.nextLine().equals("y") ) {
            return null;
        }

        switch (args[1]) {
            case "service" -> {
                //TODO Здесь будут изменения связанные с ограничение на изменение названия сервиса
                currentNoteInMainList.setIdService(args[2]);
                UsefulMethods.changingNameOfAccount(listWithNotes, args[0]);
            }
            case "login" -> currentNoteInMainList.setLogin(args[2]);
            case "password" -> currentNoteInMainList.setPassword(args[2]);
            default -> throw new UnknownArgsException("Неизвестный параметр в изменении");
        }

        return currentNoteInMainList;
    }

}
