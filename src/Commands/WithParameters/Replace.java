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
import java.util.stream.Collectors;

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

        if(args.length > 4)
            throw new UnknownArgsException("Параметров больше чем нужно");
        if(args.length < 3)
            throw new UnknownArgsException("Параметров меньше чем нужно");

        List<String> types = List.of("service", "login", "password");
        String replaceType = args.length == 3 ? args[1] : args[2];
        if(!types.contains(replaceType))
            throw new UnknownArgsException("Неизвестный параметр");

        // [serviceName] [login] [type] [newString]
        if(args.length > 3) {
            return replaceNoteByLogin(args, replaceType);
        }

        // [serviceName] [type] [newString]
        return replaceNote(args, replaceType);
    }

    // [serviceName] [login] [type] [newString]
    public String replaceNoteByLogin(String[] args, String replaceType) throws AccessNotFoundException, IncorrectValueException, UnknownArgsException {

        NoteEntity replacedNote = UsefulMethods.getAccountFromServiceByLogin(listWithNotes, args[0], args[1]);

        //Тернарный оператор с 3 условиями (вложенные)
        System.out.println( "Будут произведены следующие изменения в сервисе " + replacedNote.getIdService() + " с параметром " + replaceType + ": "
                + ( replaceType.equals("service") ? replacedNote.getIdService()
                :replaceType.equals("login") ? replacedNote.getLogin()
                :replacedNote.getPassword(true) )
                + " -> " + args[3] );

        switch (replaceType) {

            case "service" -> {
                replaceServiceName(replacedNote, args[3]); // Название нового сервиса

                return "Заменено название сервиса";
            }
            case "login" -> {
                List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]);

                // Если какого-либо аккаунта, одного сервиса, уже есть такой логин
                if (searchedServices.stream().anyMatch(note -> note.getLogin().equals(args[3])))
                    throw new IncorrectValueException("У этого сервиса такой логин уже существует");

                replaceServiceLogin(replacedNote, args[3]); // Replacement without checks

                return "Заменён логин сервиса";
            }
            case "password" -> {

                replaceServicePassword(replacedNote, args[3]); // Replacement without checks

                return "Заменён пароль сервиса";
            }
        }

        return "Что-то пошло не так";
    }

    // [serviceName] [type] [newString]
    public String replaceNote(String[] args, String replaceType) throws AccessNotFoundException, IncorrectValueException, UnknownArgsException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        if(searchedServices.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        if(searchedServices.size() > 1) {
            System.out.println("Укажите в команде логин аккаунта, который необходимо удалить");

            // Отсортировать все аккаунты сервиса по названию + вывести их названия и логины
            UsefulMethods.sortNoteEntityByServiceName( listWithNotes.stream()
                    .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase(args[0]))
                    .collect(Collectors.toList()) ).forEach((note) -> System.out.println(note.getIdService() + " -> " + note.getLogin()));

            return "Теперь введите команду";
        }

        // [serviceName] [type] [newString]
        NoteEntity replacedNote = searchedServices.get(0);
        //Тернарный оператор с 3 условиями (вложенные)
        System.out.println( "Будут произведены следующие изменения в сервисе " + replacedNote.getIdService() + " с параметром " + replaceType + ": "
                + ( replaceType.equals("service") ? replacedNote.getIdService()
                :replaceType.equals("login") ? replacedNote.getLogin()
                :replacedNote.getPassword(true) )
                + " -> " + args[2] );

        switch (replaceType) {
            case "service" -> {
                replaceServiceName(replacedNote, args[2]); // Название нового сервиса

                return "Заменено название сервиса";
            }
            case "login" -> {
                // Это единственный аккаунт у сервиса
                replaceServiceLogin(replacedNote, args[2]); // Replacement without checks

                return "Заменён логин сервиса";
            }
            case "password" -> {
                replaceServicePassword(replacedNote, args[2]); // Replacement without checks

                return "Заменён пароль сервиса";
            }
        }

        return "Что-то пошло не так";
    }

    // Обработка всех возможных проблем при изменении названия сервиса (аккаунта)
    public void replaceServiceName(NoteEntity replacedNote, String newNameReplacedNote) throws IncorrectValueException {

        // Сервис (аккаунты) у которых название совпало с названием переименуемого сервиса
        List<NoteEntity> searchedAccountsWithNewName = UsefulMethods.getAllAccountsForOneService(listWithNotes, newNameReplacedNote);

        String oldNameReplacedNote = replacedNote.getIdService(); // Старое название сервиса

        // Если аккаунтов нет, значит просто переименовываем сервис
        if(searchedAccountsWithNewName.isEmpty()) {
            replacedNote.setIdService(newNameReplacedNote);

            UsefulMethods.changingNameWhenRemove(listWithNotes, oldNameReplacedNote);

            return;
        }

        // Есть ли хоть один сервис с таким же логином как и у переименовываемого (отрицательное условие позволяет переименовывать сервис на такое же название)
        for(NoteEntity nt: searchedAccountsWithNewName) {
            if (nt.getLogin().equalsIgnoreCase(replacedNote.getLogin())
                    && !nt.getIdService().split(" ")[0].equalsIgnoreCase(replacedNote.getIdService().split(" ")[0])) {

                throw new IncorrectValueException("Такой логин уже есть");
            }
        }

        // Если изменять название одного из аккаунта на "само себя" (изменён регистр букв/ы), то нет смысла изменять нумерацию других аккаунтов
        if(replacedNote.getIdService().split(" ")[0].equalsIgnoreCase(newNameReplacedNote) && replacedNote.getIdService().contains("account")) {

            // Переименовывание название на само себя у сервиса без аккаунтов
            if(searchedAccountsWithNewName.size() == 1) {
                replacedNote.setIdService(newNameReplacedNote);
                return;
            }

            String similarName = newNameReplacedNote + " " // изменён регистр в названии
                    + replacedNote.getIdService().split(" ")[1] + " " // (№-th
                    + replacedNote.getIdService().split(" ")[2]; // account)

            replacedNote.setIdService(similarName);

            return;
        }

        replacedNote.setIdService(newNameReplacedNote); // Переименовывание

        UsefulMethods.changingNameWhenRemove(listWithNotes, oldNameReplacedNote); // Переименовывание сервиса со старым названием
        UsefulMethods.changingNameWhenAdd(listWithNotes, newNameReplacedNote); // Переименовывание сервиса с новым названием

        return;
    }

    public void replaceServiceLogin(NoteEntity replacedNote, String newLoginReplacedNote) {

        replacedNote.setLogin(newLoginReplacedNote); // Replacement without checks
    }

    public void replaceServicePassword(NoteEntity replacedNote, String newPasswordReplacedNote) throws UnknownArgsException {

        replacedNote.setPassword(newPasswordReplacedNote); // Replacement without checks
    }

}
