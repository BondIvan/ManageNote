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

import java.util.List;
import java.util.Scanner;

public class Replace implements Commands {

    private final List<NoteEntity> listWithNotes;

    public Replace() {
        this.listWithNotes = StartConsole.NOTES;
    }

    public Replace(List<NoteEntity> notes) {
        this.listWithNotes = notes;
    }

    @Override
    public String perform(String postfix) throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length < 3)
            throw new UnknownArgsException("Параметров меньше чем нужно");
        if(args.length > 3)
            throw new UnknownArgsException("Параметров больше чем нужно");
        if(!args[1].equals("service") && !args[1].equals("login") && !args[1].equals("password")) {
            throw new UnknownArgsException("Неизвестный параметр");
        }

        if(replaceNote(args)) {
            CheckingForUpdate.isUpdated = true;
            return "Изменения приняты";
        } else {
            return "Изменения не произошли";
        }

    }

    private boolean replaceNote(String[] args) throws AccessNotFoundException, IncorrectValueException, UnknownArgsException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        if(searchedServices.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        NoteEntity replacedNote;
        if(searchedServices.size() > 1) { // Если у сервиса больше одного аккаунта, выбираем с каким работать

            listWithNotes.stream() // Вывести все аккаунты сервиса + их логины
                    .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase(args[0]))
                    .forEach(note -> System.out.println(note.getIdService() + " -> " + note.getLogin()));

            System.out.print("Введите логин: ");
            String inputLogin = new Scanner(System.in).nextLine();
            replacedNote  = UsefulMethods.getAccountFromServiceByLogin(searchedServices, args[0], inputLogin);
        } else {
            replacedNote = searchedServices.get(0); // Если у сервиса нет аккаунтов, то работаем с сервисом
        }

        //Тернарный оператор с 3 условиями (вложенные)
        System.out.println( "Будут произведены следующие изменения в сервисе " + replacedNote.getIdService() + " с параметром " + args[1] + ": "
                + ( args[1].equals("service") ? replacedNote.getIdService()
                :args[1].equals("login") ? replacedNote.getLogin()
                :replacedNote.getPassword(true) )
                + " -> " + args[2] );

        //TODO Подтверждение

        switch (args[1]) {

            case "service" -> {

                return replaceServiceName(replacedNote, args[2]); // Название нового сервиса
            }
            case "login" -> {

                // Если какого-либо аккаунта, одного сервиса, уже есть такой логин
                if( searchedServices.stream().anyMatch(note -> note.getLogin().equals(args[2])) )
                    throw new IncorrectValueException("У этого сервиса такой логин уже существует");

                replaceServiceLogin(replacedNote, args[2]); // Replacement without checks
            }
            case "password" -> {

                replaceServicePassword(replacedNote, args[2]); // Replacement without checks
            }

        }

        return true;
    }

    // Обработка всех возможных проблем при изменении названия сервиса (аккаунта)
    public boolean replaceServiceName(NoteEntity replacedNote, String newNameReplacedNote) throws IncorrectValueException {

        // Сервис (аккаунты) у которых название совпало с названием переименуемого сервиса
        List<NoteEntity> searchedAccountsWithNewName = UsefulMethods.getAllAccountsForOneService(listWithNotes, newNameReplacedNote);

        String oldNameReplacedNote = replacedNote.getIdService(); // Старое название сервиса

        // Если аккаунтов нет, значит просто переименовываем сервис
        if(searchedAccountsWithNewName.isEmpty()) {
            replacedNote.setIdService(newNameReplacedNote);

            UsefulMethods.changingNameWhenRemove(listWithNotes, oldNameReplacedNote);

            return true;
        }

        // Есть ли хоть один сервис с таким же логином как и у переименовываемого (отрицательное условие позволяет переименовывать сервис на такое же название)
        for(NoteEntity nt: searchedAccountsWithNewName) {
            if (nt.getLogin().equalsIgnoreCase(replacedNote.getLogin())
                    && !nt.getIdService().split(" ")[0].equalsIgnoreCase(replacedNote.getIdService().split(" ")[0])) {

                throw new IncorrectValueException("Такой логин уже есть");
            }
        }

        replacedNote.setIdService(newNameReplacedNote); // Переименовывание

        UsefulMethods.changingNameWhenRemove(listWithNotes, oldNameReplacedNote); // Переименовывание сервиса со старым названием
        UsefulMethods.changingNameWhenAdd(listWithNotes, newNameReplacedNote); // Переименовывание сервиса с новым названием

        return true;
    }

    public void replaceServiceLogin(NoteEntity replacedNote, String newLoginReplacedNote) {

        replacedNote.setLogin(newLoginReplacedNote); // Replacement without checks
    }

    private void replaceServicePassword(NoteEntity replacedNote, String newPasswordReplacedNote) throws UnknownArgsException {

        replacedNote.setPassword(newPasswordReplacedNote); // Replacement without checks
    }

}
