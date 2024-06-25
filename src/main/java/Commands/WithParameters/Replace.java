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

import java.security.KeyStoreException;
import java.util.List;
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
    public String perform(String postfix) throws UnknownArgsException, AccessNotFoundException, IncorrectValueException, KeyStoreException {

        if(postfix.isEmpty())
            throw  new UnknownArgsException("Нет параметров");

        // Разбитие postfix-а на состовляющие (конкретные аргументы команды)
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
            return replace(args[0], args[1], replaceType, args[3]);
        }

        // [serviceName] [type] [newString]
        return replace(args[0], null, replaceType, args[2]);
    }

    public String replace(String serviceName, String serviceLogin, String replaceType, String newString) throws AccessNotFoundException, IncorrectValueException, UnknownArgsException, KeyStoreException {

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

        boolean existLogin = serviceLogin != null && !serviceLogin.isEmpty();
        if(accounts.size() > 1 && !existLogin) {
            System.out.println("Укажите в команде логин аккаунта, который необходимо изменить");
            // Отсортировать все аккаунты сервиса по названию + вывести их названия и логины
            UsefulMethods.sortNoteEntityByServiceName(listWithNotes.stream()
                    .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(serviceName))
                    .collect(Collectors.toList())).forEach((note) -> System.out.println(note.getServiceName() + " -> " + note.getLogin()));

            return "Теперь введите команду";
        }

        // [serviceName] [serviceLogin] [replaceType] : [serviceName] [replaceType]
        NoteEntity replacedNote = existLogin ?
                UsefulMethods.getAccountFromServiceByLogin(accounts, serviceName, serviceLogin) : accounts.get(0);

        //Тернарный оператор с 3 условиями (вложенные)
        System.out.println( "Будут произведены следующие изменения в сервисе " + replacedNote.getServiceName() + " с параметром " + replaceType + ": "
                + ( replaceType.equals("service") ? replacedNote.getServiceName()
                :replaceType.equals("login") ? replacedNote.getLogin()
                :replacedNote.getPassword(true) )
                + " -> " + newString );

        switch (replaceType) {
            case "service" ->
                replaceServiceName(replacedNote, newString);
            case "login" ->
                replaceServiceLogin(accounts, replacedNote, newString);
            case "password" ->
                replaceServicePassword(replacedNote, newString);
            default ->
                throw new UnknownArgsException("Не верный тип параметра");
        }

        CheckingForUpdate.isUpdated = true;

        return "Заменено " + replaceType + " у сервиса";
    }

    // Обработка всех возможных проблем при изменении названия сервиса (аккаунта)
    public void replaceServiceName(NoteEntity replacedNote, String newNameReplacedNote) throws IncorrectValueException {

        // Сервис (аккаунты) у которых название совпало с названием переименуемого сервиса
        List<NoteEntity> searchedAccountsWithNewName = UsefulMethods.getAllAccountsForOneService(listWithNotes, newNameReplacedNote);

        String oldNameReplacedNote = replacedNote.getServiceName(); // Старое название сервиса

        // Если аккаунтов нет, значит просто переименовываем сервис
        if(searchedAccountsWithNewName.isEmpty()) {
            replacedNote.setServiceName(newNameReplacedNote);

            UsefulMethods.changingNameWhenRemove(listWithNotes, oldNameReplacedNote);

            return;
        }

        // Есть ли хоть один сервис с таким же логином как и у переименовываемого (отрицательное условие позволяет переименовывать сервис на такое же название)
        for(NoteEntity nt: searchedAccountsWithNewName) {
            if (nt.getLogin().equalsIgnoreCase(replacedNote.getLogin())
                    && !nt.getServiceName().split(" ")[0].equalsIgnoreCase(replacedNote.getServiceName().split(" ")[0])) {

                throw new IncorrectValueException("Такой логин уже есть");
            }
        }

        // Если изменять название одного из аккаунта на "само себя" (изменён регистр букв/ы), то нет смысла изменять нумерацию других аккаунтов
        if(replacedNote.getServiceName().split(" ")[0].equalsIgnoreCase(newNameReplacedNote) && replacedNote.getServiceName().contains("account")) {

            // Переименовывание название на само себя у сервиса без аккаунтов
            if(searchedAccountsWithNewName.size() == 1) {
                replacedNote.setServiceName(newNameReplacedNote);
                return;
            }

            String similarName = newNameReplacedNote + " " // изменён регистр в названии
                    + replacedNote.getServiceName().split(" ")[1] + " " // (№-th
                    + replacedNote.getServiceName().split(" ")[2]; // account)

            replacedNote.setServiceName(similarName);

            return;
        }

        replacedNote.setServiceName(newNameReplacedNote); // Переименовывание

        UsefulMethods.changingNameWhenRemove(listWithNotes, oldNameReplacedNote); // Переименовывание сервиса со старым названием
        UsefulMethods.changingNameWhenAdd(listWithNotes, newNameReplacedNote); // Переименовывание сервиса с новым названием
    }

    public void replaceServiceLogin(List<NoteEntity> accounts, NoteEntity replacedNote, String newLoginReplacedNote) throws IncorrectValueException {

        // Проверяет сушествует ли сервис с таким же логином
        if(accounts.stream().anyMatch(note -> note.getLogin().equalsIgnoreCase(newLoginReplacedNote)))
            throw new IncorrectValueException("У этого сервиса такой логин уже существует");

        replacedNote.setLogin(newLoginReplacedNote);
    }

    public void replaceServicePassword(NoteEntity replacedNote, String newPasswordReplacedNote) throws UnknownArgsException {

        replacedNote.setPassword(newPasswordReplacedNote);
    }

}
