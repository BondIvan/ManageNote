package Commands;

import Entity.NoteEntity;

import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Save extends Commands {

    /***

     -save-

     ***/


    private final String pathToSave; // Путь перезаписываемого файла

    public Save(String pathToSave) {
        this.pathToSave = pathToSave;
    }

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Вы уверены, что хотите сохранить файл ? (y/n)");

            if (scanner.nextLine().equalsIgnoreCase("y"))
                return saving();

            return "Файл не сохранён";
        }

        return "Изменений не произошло";
    }

    public String saving() throws IOException {

        FileWriter fileWriter = new FileWriter(pathToSave);

        // Построчная запись в файл всех объктов NoteEntity
        for (NoteEntity note : TestingClass.notes)
            fileWriter.write(note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword() + "\n\n");

        fileWriter.close();

        CheckingForUpdate.isUpdated = false;

        return "Файл сохранён";
    }

}
