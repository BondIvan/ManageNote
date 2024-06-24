package Commands.WithOrWithoutParameters;

import Commands.Commands;
import Encrypting.Security.Storage.PasswordStorage;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.UsefulMethods;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckFiles implements Commands {

    @Override
    public String perform(String postfix) throws IOException, UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        boolean resultChecking;
        if(!postfix.isEmpty()) {

            String[] args = UsefulMethods.makeArgsTrue(postfix);

            if (args.length > 1)
                throw new UnknownArgsException("Параметров больше чем нужно");
            if (!args[0].equals("false") && !args[0].equals("true"))
                throw new UnknownArgsException("Неверный параметр");

            // [true/false]
            if (args[0].equals("true"))
                resultChecking = inspect(true);
            else
                resultChecking = inspect(false);

        // ['empty']
        } else {
            resultChecking = inspect(true);
        }

        if(resultChecking)
            return "Все проверки прошли успешно";
        else
            return "Проверка не прошла успешно";

    }

    public boolean inspect(boolean simulationDelay) throws IOException {

        List<NoteEntity> list = StartConsole.NOTES;
        List<String> ids = list.stream()
                .map(note -> note.getId().toLowerCase())
                .toList();

        try {
            PasswordStorage passwordStorage = new PasswordStorage();
            List<String> aliases = passwordStorage.getAliases();

            if(simulationDelay)
                System.out.println("Первая проверка:");
            boolean checkKeys =  checkKeys(list, simulationDelay);

            if(simulationDelay)
                System.out.println("Вторая проверка:");
            boolean checkAliases = checkAliases(aliases, ids, simulationDelay);

            return checkKeys && checkAliases;

        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            System.out.println("Не удалось провести проверку. Ошибка связана проблемой с вызовом хранилища, текст ошибки: " + e.getMessage());
            return false;
        }

    }

    // Проверка, соответствует ли ключ паролю
    private boolean checkKeys(List<NoteEntity> list, boolean simulationDelay) throws KeyStoreException {

        if (list.isEmpty())
            return false;

        List<String> passwordWithNullResult = new ArrayList<>();

        // Без имитации задержки
        if(!simulationDelay) {
            for (NoteEntity note : list)
                if (note.getPassword(true) == null)
                    passwordWithNullResult.add(note.getPassword(false));

            System.out.println(passwordWithNullResult.isEmpty() ? "" : passwordWithNullResult.toString());
            return passwordWithNullResult.isEmpty();
        }

        // С имитацией задержки для демонстрации прогресс-бара
        int count = 1;
        int listSize = list.size();
        for (NoteEntity note : list) {
            if (note.getPassword(true) != null)
                System.out.print("\rПроверено: " + (count++) + " ... " + listSize);
            else
                passwordWithNullResult.add(note.getPassword(false));

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        System.out.println(passwordWithNullResult.isEmpty() ? "\nПроверка прошла успешно" : passwordWithNullResult.toString());

        return passwordWithNullResult.isEmpty();
    }

    // Проверка соответствует id из файла id в keyStore
    private boolean checkAliases(List<String> aliases, List<String> idsFromFile, boolean simulationDelay) {

        if(aliases.isEmpty()) {
            System.out.println("List with aliases is empty");
            return false;
        }
        if(idsFromFile.isEmpty()) {
            System.out.println("List with ids is empty");
            return false;
        }

        List<String> alias = new ArrayList<>(List.copyOf(aliases));
        List<String> file = new ArrayList<>(List.copyOf(idsFromFile));

        alias.removeAll(idsFromFile);
        file.removeAll(aliases);

        // С имитацией задержки для демонстрации прогресс-бара, если false - нет имитации задержки
        if(simulationDelay) {
            int listSize = aliases.size(); //Math.max(aliases.size(), idsFromFile.size());
            for (int i = 0; i < aliases.size(); i++) {
                System.out.print("\rПроверено: " + (i + 1) + " ... " + listSize);

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            System.out.println();
        }

        StringBuilder buffer = new StringBuilder();
        if( alias.isEmpty() && file.isEmpty() ) {
            if(simulationDelay)
                System.out.println("Проверка прошла успешно\n");

            return true;
        }

        if ( !alias.isEmpty() )
            buffer.append("\nЭти элементы отсутствуют в списке ids из файла: ").append(Arrays.toString(alias.toArray()));
        if( !file.isEmpty() )
            buffer.append("\nЭти элементы отсутствуют в списке aliases: ").append(Arrays.toString(file.toArray())).append("\n");

        System.out.println(buffer);

        return false;
    }

}
