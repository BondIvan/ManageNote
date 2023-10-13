package Commands.WithoutParameters;

import Commands.CommandsWithoutParameters;
import Entity.NoteEntity;
import Tools.CheckingForUpdate;

import java.util.List;

public class Exit extends CommandsWithoutParameters {

    private final String pathToSave; // Путь перезаписываемого файла

    private final List<NoteEntity> listWithNotes; // Что сохранять

    public Exit(String pathToSave, List<NoteEntity> listWithNotes) {
        this.pathToSave = pathToSave;
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated) {
            CommandsWithoutParameters save = new Save(pathToSave, listWithNotes);
            System.out.println( save.perform() );
        }

        System.out.println("Выход из приложения");

        System.exit(0);
        return "";
    }

}
