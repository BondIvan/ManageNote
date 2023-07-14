package Commands;

import Entity.NoteEntity;

import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Save extends Commands {

    /***

     -save-

     ***/

    private final String pathToSave; // Путь перезаписываемого файла

    // Что сохранять (Позволяет использовать этот класс в ином "контексте" (Например: в случае изменения метода шифрования))
    private final List<NoteEntity> notes;

    public Save(String pathToSave, List<NoteEntity> listNotes) {
        this.pathToSave = pathToSave;
        this.notes = listNotes;
    }

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Вы уверены, что хотите сохранить файл ? (y/n)");

            if (scanner.nextLine().equalsIgnoreCase("y"))
                return saving(notes);

            return "Файл не сохранён";
        }

        return "Изменений не произошло";
    }

    private String saving(List<NoteEntity> listNotesForSave) throws IOException {

        FileWriter fileWriter = new FileWriter(pathToSave);

        // Построчная запись в файл всех объктов NoteEntity
        for (NoteEntity note : listNotesForSave)
            fileWriter.write(note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword() + "\n\n");

        fileWriter.close();

        CheckingForUpdate.isUpdated = false;

        return "Файл сохранён";
    }

}
