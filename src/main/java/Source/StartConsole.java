package Source;

import Commands.CommandFactory;
import Commands.Commands;
import Commands.WithOrWithoutParameters.GetAll;
import Commands.WithParameters.Add;
import Commands.WithParameters.Delete;
import Commands.WithParameters.Get;
import Commands.WithParameters.Replace;
import Commands.WithoutParameters.CopyFile;
import Commands.WithoutParameters.Exit;
import Commands.WithoutParameters.Help;
import Commands.WithoutParameters.Save;
import Encrypting.Secure.Validation;
import Entity.NoteEntity;
import OptionsExceptions.CommandNotFoundException;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.DestroyFailedException;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StartConsole {

    /***

     При запуске считывает файл с доступами в список. На протяжении выполнения программы, все записи хранятся в этом списке.
     По окончанию выполнения программы, файл перезапишется c информацией, которую содержит список.

     Всё редактирование доступов происходит в списке.

     ***/

    public static final String PATH = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Txt\\" +
            "TestingAccess.txt"; // Тестовый файл
            //"Access.txt"; // Рабочий файл

    public static List<NoteEntity> NOTES = new ArrayList<>(); // Список всех записей (сервисов)

    public static void main(String[] args) throws Exception {

        // Мастер-пароль
        masterPassword();

        // Чтение сервисов из файла
        NOTES = UsefulMethods.getAllNoteFromFile(PATH);

        // Указание с каким файлом идёт работа
        String currentFile = Paths.get(PATH).getFileName().toString();
        System.out.println("Запущено с файлом: " + currentFile);

        // Заполнение словаря для автокоррекции
        Dictionaries dictionaries = new Dictionaries();
        dictionaries.fillingDictionaries(NOTES);

        // Регистрация команд в фабрику
        CommandFactory factory = assembleCommandFabric();

        while (true) {

            console(factory, dictionaries);

        }

    }

    private static void masterPassword() throws DestroyFailedException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        Scanner inputLine = new Scanner(System.in);
        Validation validation = new Validation();
        if(validation.isExist()) {
            int max_attempts = 3;
            for (int i = 1; i <= max_attempts; i++) {
                System.out.print("Введите мастер-пароль: ");
                String input = inputLine.nextLine();
                if (validation.checkInputPassword(input)) {
                    System.out.println("Мастер-пароль верный");
                    input = null;
                    break;
                } else {
                    System.out.println("Мастер-пароль НЕ верный. Осталось попыток - " + (max_attempts - i));
                }

                if (i == max_attempts) {
                    System.out.println("Попыток больше нет");
                    System.exit(1);
                }
            }
        } else {
            System.out.print("Задайте мастер-пароль: "); // asd
            char[] inputPassword = inputLine.nextLine().toCharArray();
            validation.createMasterPassword(inputPassword);
            Arrays.fill(inputPassword, '\0');
        }
    }

    private static CommandFactory assembleCommandFabric() {
        CommandFactory commandFactory = new CommandFactory();

        // Команды с и без аргументов
        commandFactory.registerCommand("getall", GetAll.class);

        // Команды с аргументами
        commandFactory.registerCommand("add", Add.class);
        commandFactory.registerCommand("delete", Delete.class);
        commandFactory.registerCommand("get", Get.class);
        commandFactory.registerCommand("replace", Replace.class);

        // Команды без аргументов
        commandFactory.registerCommand("copyfile", CopyFile.class);
        commandFactory.registerCommand("exit", Exit.class);
        commandFactory.registerCommand("help", Help.class);
        commandFactory.registerCommand("save", Save.class);

        return commandFactory;
    }

    private static void console(CommandFactory factory, Dictionaries dictionaries) {
        Scanner inputLine = new Scanner(System.in);
        System.out.print("Введите команду: ");
        String input = inputLine.nextLine().trim(); // Введённая строка

        if (input.isEmpty())
            return; //continue;

        String prefix = input.split(" ")[0]; // Введённая команда
        String postfix = input.substring(prefix.length()).trim(); // Аргументы введённой команды

        try {
            Commands command = factory.getCommand(prefix);

            if(command == null)
                throw new CommandNotFoundException("Такой команды нет");

            if(command instanceof Delete) {
                System.out.println("Действительно удалить этот сервис? (y/n) " + postfix);
                if(!inputLine.nextLine().equals("y")) {
                    System.out.println("Удаление НЕ произошло");
                    return; //continue;
                }
            }

            System.out.println(command.perform(postfix) + "\n");

            dictionaries.fillingDictionaries(NOTES); // Обновление словаря, после изменения главного списка (удаление, добавление, изменение сервиса)
        } catch (Exception e) {
            System.out.println("Ошибка с сообщением: " + e.getMessage());
        }

    }
}
