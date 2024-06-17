package Encrypting.Chek;

import Encrypting.Security.Storage.PasswordStorage;
import Entity.NoteEntity;
import Source.StartConsole;
import Tools.UsefulMethods;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckFiles {

    public void inspect() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {

        List<NoteEntity> list = UsefulMethods.getAllNoteFromFile(StartConsole.PATH);
        List<String> ids = list.stream()
                .map(note -> note.getId().toLowerCase())
                .toList();

        PasswordStorage passwordStorage = new PasswordStorage();
        List<String> aliases = passwordStorage.getAliases();

        System.out.println("Первая проверка:");
        System.out.println( checkKeys(list) );

        System.out.println("Вторая проверка:");
        System.out.println( checkAliases(aliases, ids) );

    }

    // Проверка, соответствует ли ключ паролю
    private String checkKeys(List<NoteEntity> list) {

        if (list.isEmpty())
            return "List is empty";

        List<String> passwordWithNullResult = new ArrayList<>();
        int count = 1;
        int listSize = list.size();
        for (NoteEntity note : list) {
            if (note.getPassword(true) != null)
                System.out.print("\rПроверено: " + (count++) + " ... " + listSize);
            else
                passwordWithNullResult.add(note.getPassword(false));

            // Имитация задержки для демонстрации прогресс-бара
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        return passwordWithNullResult.isEmpty() ? "\nПроверка прошла успешно" : passwordWithNullResult.toString();
    }

    // Проверка соответствует id из файла id в keyStore
    private String checkAliases(List<String> aliases, List<String> idsFromFile) {

        if(aliases.isEmpty())
            return "List with aliases is empty";
        if(idsFromFile.isEmpty())
            return "List with ids is empty";

        List<String> alias = new ArrayList<>(List.copyOf(aliases));
        List<String> file = new ArrayList<>(List.copyOf(idsFromFile));

        alias.removeAll(idsFromFile);
        file.removeAll(aliases);

        int listSize = aliases.size(); //Math.max(aliases.size(), idsFromFile.size());
        for(int i = 0; i < aliases.size(); i++) {
            System.out.print("\rПроверено: " + (i+1) + " ... " + listSize);

            // Имитация задержки для демонстрации прогресс-бара
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        StringBuilder buffer = new StringBuilder();
        if( alias.isEmpty() && file.isEmpty() )
            return "\nПроверка прошла успешно\n";

        if ( !alias.isEmpty() )
            buffer.append("\nЭти элементы отсутствуют в списке ids из файла: ").append(Arrays.toString(alias.toArray()));
        if( !file.isEmpty() )
            buffer.append("\nЭти элементы отсутствуют в списке aliases: ").append(Arrays.toString(file.toArray())).append("\n");

        return buffer.toString();
    }

}
