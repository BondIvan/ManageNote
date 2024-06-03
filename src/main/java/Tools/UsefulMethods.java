package Tools;

import Entity.NoteEntity;

import OptionsExceptions.AccessNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UsefulMethods {

    // Изменяет номера аккаунтов при добавлении аккаунта/сервиса (Максимум 10 аккаунтов у 1 сервиса)
    public static void changingNameWhenAdd(List<NoteEntity> listWithNotes, String serviceName) {

        String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 аккаунтов максимум

        int count = (int) listWithNotes.stream() // Определить, сколько аккаунтов существует у сервиса
                .filter(note -> note.getServiceName().toLowerCase().contains(serviceName.toLowerCase()))
                .count();

        List<NoteEntity> accounts = listWithNotes.stream() // Список аккаунтов без нумерации
                .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(serviceName))
                .filter(note -> !note.getServiceName().contains("account"))
                .toList(); // toList возвращает неизменяемый список (immutable)

        // Если до переименовывания сервиса не было аккаунтов
        if(count == 2) {
            accounts.get(0).setServiceName(serviceName + " (1-st account)");
        }

        // Если такого сервиса вообще не было, то нумерацию добалять не нужно
        String serviceWithNewSerialNumber = count > 1 ?
                serviceName + " (" + numberOfAccount[count-1] + " account)" : serviceName;

        Optional<NoteEntity> account = accounts.stream() // Взять аккаунт без нумерации
                .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(serviceName))
                .filter(note -> !note.getServiceName().contains("account"))
                .findFirst();

        account.ifPresent(noteEntity -> noteEntity.setServiceName(serviceWithNewSerialNumber));
    }

    // Изменяет номера аккаунтов при удалении одного из аккаунта у сервиса (Максимум 10 аккаунтов у 1 сервиса)
    public static void changingNameWhenRemove(List<NoteEntity> listWithNotes, String serviceName) {

        String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 аккаунтов максимум

        List<NoteEntity> accounts = listWithNotes.stream() // Все аккаунты сервиса
                .filter(note -> note.getServiceName().toLowerCase().contains(serviceName.split(" ")[0].toLowerCase()))
                .collect(Collectors.toList()); // Так, потому что .toList() возвращает неизменяемый список (immutable)

        // Если удалён сервис без аккаунтов
        if(accounts.isEmpty())
            return;

        // Если это последний аккаунт, то удалить №-th account
        if(accounts.size() == 1) {
            String nameWithoutNumber = accounts.get(0).getServiceName().split(" ")[0];
            accounts.get(0).setServiceName( nameWithoutNumber );

            return;
        }

        // Отсортировать аккаунты по названию (по номеру)
        sortNoteEntityByServiceName(accounts);

        // Если текущее название не содержит номер по порядку (проверяется с помощью массива numberOfAccount с счётчиком k),
        // то заменяем следующий номер по счёту (Exp 1: если у текущего 3-rd, а должно быть 2-nd).
        for(int i = 0, k = 0; i < accounts.size(); i++) {

            String currName = accounts.get(i).getServiceName();
            if( !currName.contains(numberOfAccount[k]) ) {
                accounts.get(i).setServiceName( currName.replace(numberOfAccount[k+1], numberOfAccount[k]) ); // Для Exp 1: заменяем 3-rd на 2-nd
            }
            k++;
        }

    }

    // Получение всех аккаунтов одного сервиса
    public static List<NoteEntity> getAllAccountsForOneService(List<NoteEntity> listWithNotes, String serviceName) {

        List<NoteEntity> otherAccounts = listWithNotes.stream()
                .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(serviceName))
                .collect(Collectors.toList());

        return otherAccounts;
    }

    // Если у сервиса несколько аккаунтов, этот метод позволяет получить доступ к конкретному аккаунту по логину
    public static NoteEntity getAccountFromServiceByLogin(List<NoteEntity> listWithNotes, String searchedName, String searchedLogin)
            throws AccessNotFoundException {

        // Фильтр по названию, потом фильтр по логину, чтобы найти необходимый аккаунт
        NoteEntity noteEntity = listWithNotes.stream()
                .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(searchedName))
                .filter(note -> note.getLogin().equalsIgnoreCase(searchedLogin))
                .findFirst()
                .orElseThrow(() -> new AccessNotFoundException("Неправильный логин аккаунта"));

        return noteEntity;
    }

    // Убирает лишние пробелы между аргументами команды
    public static String[] makeArgsTrue(String postfix) {

        // postfix = get___name.com -> "get", "", "", "name.com"

        List<String> list = new ArrayList<>(Arrays.stream(postfix.split(" ")).toList());

        list.removeIf(String::isEmpty); // aka цикл, удалить объект если он пустой

        return list.toArray(new String[0]); // В душе не знаю, почему 0
    }

    // Чтение всех сервисов из файла и схранение их в виде NoteEntity в список
    public static List<NoteEntity> getAllNoteFromFile(String path) throws IOException {

        //TODO Переписать данный метод

        FileReader fileReader = new FileReader(path);
        Scanner file = new Scanner(fileReader);
        List<NoteEntity> allNoteFromFile = new ArrayList<>();

        String data; // Текущая строка, то есть (название сервиса)
        while(file.hasNextLine()) {

            data = file.nextLine();
            if(!data.isEmpty()) {
                String id = file.nextLine().substring(4);
                String login = file.nextLine().substring(7);
                String password = file.nextLine().substring(10);

                NoteEntity noteEntity = new NoteEntity(data, id, login, password);
                allNoteFromFile.add(noteEntity);
            }

        }

        file.close();
        fileReader.close();

        return allNoteFromFile;
    }

    // Сортирует массив NoteEntity по названию сервиса
    public static List<NoteEntity> sortNoteEntityByServiceName(List<NoteEntity> listWithNotes) {

        Comparator<NoteEntity> comparator = new Comparator<NoteEntity>() {
            @Override
            public int compare(NoteEntity note1, NoteEntity note2) {
                return note1.getServiceName().compareTo(note2.getServiceName());
            }
        };

        listWithNotes.sort(comparator);

        return listWithNotes;
    }

    //TODO Что это???
    public static void replaceFirstNoteToSecondNote(List<NoteEntity> listWithNotes, NoteEntity firstNote, NoteEntity secondNote) {

        int positionFirstNoteInList = listWithNotes.indexOf(firstNote);

        listWithNotes.set( positionFirstNoteInList, secondNote );
    }

    // Список с уникальными названиями сервисов (без учёта аккаунтов)
    public static List<String> getAllUniqueServiceName(List<NoteEntity> listWithNotes) {

        Set<String> allUniqueServiceName = new HashSet<>();

        for(NoteEntity note: listWithNotes) {
            String serviceName = note.getServiceName();
            if(serviceName.contains("account"))
                serviceName = serviceName.split(" ")[0];

            allUniqueServiceName.add(serviceName);
        }

        return new ArrayList<>(allUniqueServiceName);
    }
}
