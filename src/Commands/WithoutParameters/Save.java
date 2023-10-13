package Commands.WithoutParameters;

import Commands.CommandsWithoutParameters;
import Entity.NoteEntity;

import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Save extends CommandsWithoutParameters {

    private final String pathToSave; // Путь перезаписываемого файла

    // Что сохранять (Позволяет использовать этот класс в ином "контексте" (Например: в случае изменения метода шифрования))
    private List<NoteEntity> listWithNotes;

    public Save(String pathToSave, List<NoteEntity> listNotes) {
        this.pathToSave = pathToSave;
        this.listWithNotes = listNotes;
    }

    public Save(String pathToSave) {
        this.pathToSave = pathToSave;
    }

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated)
            return "Файл сохранён: " + saving(pathToSave, listWithNotes);

        return "Изменений не произошло";
    }

    private boolean saving(String path, List<NoteEntity> listWithNotesForSave) throws IOException {

        Scanner confirm = new Scanner(System.in);
        System.out.println("Сохранить файл ? (y/n)");

        if( !confirm.nextLine().equals("y") ) {
            return false;
        }

        FileWriter fileWriter = new FileWriter(path);
        // Построчная запись в файл всех объктов NoteEntity
        for (NoteEntity note : listWithNotesForSave)
            fileWriter.write(note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword(false) + "\n\n");
        fileWriter.close();

        CheckingForUpdate.isUpdated = false;

        return true;
    }

}
