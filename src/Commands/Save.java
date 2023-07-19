package Commands;

import Entity.NoteEntity;

import OptionsExceptions.WrongPostfixMethodException;

import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import Source.StartConsole;

public class Save extends Commands {

    /***

     -save-

     ***/

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

        if(CheckingForUpdate.isUpdated) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Вы уверены, что хотите сохранить файл ? (y/n)");

            if (scanner.nextLine().equalsIgnoreCase("y"))
                return saving(listWithNotes, pathToSave);

            return "Файл не сохранён";
        }

        return "Изменений не произошло";
    }

    @Override
    public String perform(String postfix) throws WrongPostfixMethodException {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

    private String saving(List<NoteEntity> listWithNotesForSave, String path) throws IOException {

        FileWriter fileWriter = new FileWriter(path);

        // Построчная запись в файл всех объктов NoteEntity
        for (NoteEntity note : listWithNotesForSave)
            fileWriter.write(note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword(false) + "\n\n");

        fileWriter.close();

        CheckingForUpdate.isUpdated = false;

        return "Файл сохранён";
    }

}
