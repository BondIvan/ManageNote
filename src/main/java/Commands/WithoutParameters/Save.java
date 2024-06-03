package Commands.WithoutParameters;

import Commands.Commands;
import Entity.NoteEntity;
import Source.StartConsole;
import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Save implements Commands {

    private final String pathToSave; // Куда сохранять
    private final List<NoteEntity> listWithNotes; // Что сохранять

    public Save() {
        this.pathToSave = StartConsole.PATH;
        this.listWithNotes = StartConsole.NOTES;
    }

    public Save(String pathToSave, List<NoteEntity> notes) {
        this.pathToSave = pathToSave;
        this.listWithNotes = notes;
    }

    @Override
    public String perform(String postfix) throws IOException {

        if(CheckingForUpdate.isUpdated)
            return "Файл сохранён: " + saving(listWithNotes);

        return "Изменений не произошло";
    }

    private boolean saving(List<NoteEntity> listWithNotesForSave) throws IOException {

        Scanner confirm = new Scanner(System.in);
        System.out.println("Сохранить файл ? (y/n)");

        if( !confirm.nextLine().equals("y") ) {
            return false;
        }

        FileWriter fileWriter = new FileWriter(pathToSave);
        // Построчная запись в файл всех объктов NoteEntity
        for (NoteEntity note : listWithNotesForSave)
            fileWriter.write(note.getServiceName() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword(false) + "\n\n");
        fileWriter.close();

        CheckingForUpdate.isUpdated = false;

        return true;
    }

}
