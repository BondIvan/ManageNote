package Commands.WithoutParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.WrongPostfixMethodException;
import Source.StartConsole;
import Tools.CheckingForUpdate;

import java.util.List;

public class Exit extends Commands {

    private final String pathToSave; // Путь перезаписываемого файла

    private final List<NoteEntity> listWithNotes; // Что сохранять

    public Exit(String pathToSave, List<NoteEntity> listWithNotes) {
        this.pathToSave = pathToSave;
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated) {
            Commands save = new Save(pathToSave, listWithNotes);
            System.out.println( save.perform() );
        }

        System.out.println("Выход из приложения");

        System.exit(0);
        return "";
    }

    @Override
    public String perform(String postfix) throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }
}
