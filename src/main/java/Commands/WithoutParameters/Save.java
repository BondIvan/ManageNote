package Commands.WithoutParameters;

import Commands.Commands;
import Entity.NoteEntity;
import Source.StartConsole;
import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStoreException;
import java.util.List;

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
    public String perform(String postfix) {

        if(CheckingForUpdate.isUpdated)
            return "Файл сохранён: " + saving(listWithNotes);

        return "Изменений не произошло";
    }

    private boolean saving(List<NoteEntity> listWithNotesForSave) {

//        Scanner confirm = new Scanner(System.in);
//        System.out.println("Сохранить файл ? (y/n)");
//
//        if( !confirm.nextLine().equals("y") ) {
//            return false;
//        }

        // Построчная запись в файл всех объктов NoteEntity
        try (FileWriter fileWriter = new FileWriter(pathToSave)) {
            for (NoteEntity note : listWithNotesForSave)
                fileWriter.write(note.getServiceName()
                        + "\nid: " + note.getId()
                        + "\nLogin: " + note.getLogin()
                        + "\nPassword: " + note.getPassword(false) + "\n\n");
        } catch (KeyStoreException | IOException e) {
            System.out.println("Ошибка записи в файл. " + e.getMessage());
        }

        CheckingForUpdate.isUpdated = false;

        return true;
    }

}
