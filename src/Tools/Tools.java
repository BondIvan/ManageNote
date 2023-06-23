package Tools;

import Commands.TestingClass;

import Entity.NoteEntity;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Tools {

    // Изменяет номер аккаунта у сервиса + удаляет из названия сервиса (№-th account), если он последний. (Максимум 10 аккаунтов у 1 сервиса)
    public static void changingNameOfAccount(String serviceName) {
        String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 "аккаунтов" максимум

        int firstAccountService = 0;
        int accountNeededNumber = 0;
        for(int i = 0, k = 0, accountLeft_count = 0; i < TestingClass.notes.size(); i++) {

            String currentName = TestingClass.notes.get(i).getIdService(); // Текущее рассмативаемое название аккаунта

            if(currentName.toLowerCase().contains(serviceName.toLowerCase())) {
                if (currentName.contains("account")) { // Если сервис содержит искомый сервис + у него в названии есть слово "аккаунт" (то есть не один аккаунт)

                    accountLeft_count++; // Считает сколько аккаунтов осталось у сервиса

                    if(accountLeft_count <= 1) { // Нет смысла запоминать позицию первого аккаунта, если их больше 1, удалять из названия (№-th account) не нужно
                        firstAccountService = i; // Запоминает первый встречанный аккаунт, для того, в случае, если это последний аккаунт,
                        // нужно будет удалить из названия аккаунта (№-th account)
                    }

                    // Если текущее название не содержит номер по порядку (проверяется с помощью массива numberOfAccount с счётчиком k),
                    // то заменяем следующий номер по счёту (Exp 1: если у текущего 3-rd, а должно быть 2-nd).
                    if(!currentName.contains(numberOfAccount[k])) {
                        // Для Exp 1: заменяем 3-rd на 2-nd
                        TestingClass.notes.get(i).setIdService(currentName.replace(numberOfAccount[k+1], numberOfAccount[k]));
                    }

                    k++; // Счётчик для массива numberOfAccount
                } else {
                    accountNeededNumber = i;
                }

            }

            // При переименовывании: новое название переименовонного сервиса есть уже в другом сервисе
            // (Exp: Test replace -> Vk.com, Vk.com уже содержит № кол. аккаунтов)
            if(i + 1 == TestingClass.notes.size() && accountNeededNumber > 0) {

                TestingClass.notes.get(accountNeededNumber).setIdService( serviceName + " " + numberOfAccount[accountLeft_count] + " account" );

               //TestingClass.notes.get(save).setIdService( TestingClass.notes.get(save).getIdService().split(" ")[0] );
            }

            // Если достигнут последний аккаунт из ВСЕХ И количество аккаунтов сервиса после удаления остался 1, то удалить у названия сервиса (№-th account)
            if(i + 1 == TestingClass.notes.size() && accountLeft_count == 1) {
                TestingClass.notes.get(firstAccountService).setIdService( TestingClass.notes.get(firstAccountService).getIdService().split(" ")[0] );
            }

        }

    }

    // Получение всех аккаунтов одного сервиса
    public static List<NoteEntity> getAllAccounts(String serviceName) {
        ArrayList<NoteEntity> otherAccounts = new ArrayList<>(); // Список аккаунтов одного сервиса

        for(NoteEntity note: TestingClass.notes) {
            String currentServiceName = note.getIdService();
            if(currentServiceName.split(" ")[0].equalsIgnoreCase(serviceName)) { // Сравнивается первое слово текущего сервиса с требуемым
                otherAccounts.add(note);
            }
        }

        return otherAccounts;
    }

    // Поиск "похожего" сервиса по совпадению первых букв
    public static String getSimilar(String[] args) {
        StringBuilder resultSimilar = new StringBuilder();
        for(NoteEntity note: TestingClass.notes) {

            if( note.getIdService().substring(0, 1).equalsIgnoreCase(args[0].substring(0, 1)) ) // substring(0, 1) первая буква названия сервиса
                resultSimilar.append(note.getIdService()).append("\n");

        }

        return resultSimilar.toString();
    } // Ok

    // Если у сервиса несколько аккаунтов, этот метод позволяет получить доступ к конкретному аккаунту по логину
    // Или же у названия сервиса 2 слова или больше, можно ввести первое слово названия и затем ввести логин этого сервиса

    public static NoteEntity getWithLogin(String searchedName) throws Exception {

        List<NoteEntity> otherAccounts = new ArrayList<>(); // Список аккаунтов одного сервиса

        for(NoteEntity note: TestingClass.notes) {
            String currName = note.getIdService();
            if( currName.split(" ")[0].equalsIgnoreCase(searchedName) ) { // Сравнивается первое слово текущего сервиса с требуемым
                otherAccounts.add(note);
            }
        }

        if(!otherAccounts.isEmpty()) { // Если несколько аккаунтов у сервиса, то необходимый аккаунт можно получить по логину

            for (NoteEntity str : otherAccounts)
                System.out.println(str.getIdService() + " -> " + str.getLogin());

            System.out.println("Выбирете учётную запись из логинов: ");
            Scanner scanner = new Scanner(System.in); // Для ввода необходимого логина

            String inputLogin = scanner.nextLine().trim();
            for (NoteEntity note : otherAccounts) {

                // Если введённый логин совпадёт с логином одного из аккаунта
                if (note.getLogin().equalsIgnoreCase(inputLogin)) { //TODO Посмотреть для чего это вообще
                    // Удаление аккаунта из otherAccounts для изменения его размера, чтобы можно было определить сколько ещё аккаунтов у этого сервиса
                    otherAccounts.remove(note);

                    return note;
                }
            }
        }

        throw new Exception("Такой записи нет");
    }

    // Убирает лишние пробелы между аргументами команды
    public static String[] makeArgsTrue(String postfix) {

        // postfix = get___name.com -> "get", "", "", "name.com"

        List<String> list = new ArrayList<>(Arrays.stream(postfix.split(" ")).toList());

        list.removeIf(String::isEmpty); // aka цикл, удалить объект если он пустой

        return list.toArray(new String[0]); // В душе не знаю, почему 0
    } // Ok

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
                NoteEntity noteEntity = new NoteEntity(data, file.nextLine().substring(7), file.nextLine().substring(10));
                allNoteFromFile.add(noteEntity);
            }

        }

        file.close();
        fileReader.close();

        return allNoteFromFile;
    }

}
