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

        if(args.length < 3)
            throw new UnknownArgsException("Параметров меньше чем нужно");
        if(args.length > 3)
            throw new UnknownArgsException("Параметров больше чем нужно");
        if(!args[1].equals("service") && !args[1].equals("login") && !args[1].equals("password")) {
            throw new UnknownArgsException("Неизвестный параметр");
        }

        if(replaceNote(args)) {
            return "Изменения не произошли";
        } else {
            CheckingForUpdate.isUpdated = true;
            return "Изменения приняты";
        }

    }

    private boolean replaceNote(String[] args) throws AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> searchedServices = UsefulMethods.getAllAccountsForOneService(listWithNotes, args[0]); // Содержит необходимы-й/е аккаунт-/ы

        if(searchedServices.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.autoCorrect(args[0], Dictionaries.uniqueServiceNames);
            System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        NoteEntity replacedNote;
        if(searchedServices.size() > 1) { // Если у сервиса больше одного аккаунта (от двух и больше), выбираем с каким работать
            // Вывести все аккаунты сервиса + их логины
            listWithNotes.stream()
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

                // Если изменить название Vk.com на название vk.com
                if(searchedServices.get(0).getIdService().split(" ")[0].equals(args[2]))
                    throw new IncorrectValueException( "Нельзя менять название сервиса на такое же" );

                return replaceServiceName(args[2], replacedNote); // Название нового сервиса

            }
            case "login" -> {
                // Если какого-либо аккаунта, одного сервиса, уже есть такой логин
                if( searchedServices.stream().anyMatch(note -> note.getLogin().equals(args[2])) )
                    throw new IncorrectValueException("У этого аккаунта такой логин уже существует");

                replacedNote.setLogin(args[2]);
            }
            case "password" -> {
                // Nothing right now
            }

        }

        return true;
    }

    // Обработка всех возможных проблем при изменении названия сервиса (аккаунта)
    private boolean replaceServiceName(String newServiceName, NoteEntity replacedNote) {
        //TODO Написать/переписать метод в usefulMethod который будет менять нумерацию аккаунтов у одного сервиса
        // (для удаления уже есть, нужна для добавления аккаунта)

        // Сервис (аккаунты) у которых название совпало с названием переименуемого сервиса
        List<NoteEntity> searchedAccountsWithNewName = UsefulMethods.getAllAccountsForOneService(listWithNotes, newServiceName);

        if(searchedAccountsWithNewName.isEmpty()) {
            replacedNote.setIdService(newServiceName);

            return true;
        }


        if(searchedAccountsWithNewName.size() == 1) {

            boolean has = searchedAccountsWithNewName.stream()
                    .anyMatch(note -> note.getLogin().equalsIgnoreCase(replacedNote.getLogin()));

            if(!has)
                return false; // Такой логин уже есть

            replacedNote.setIdService(newServiceName);

            // UsefulMethods -> изменение порядкового номера

        }


        return false;
    }

}
