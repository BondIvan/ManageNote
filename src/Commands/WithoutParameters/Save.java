package Commands.WithoutParameters;

import Commands.Commands;
import Entity.NoteEntity;

import OptionsExceptions.WrongPostfixMethodException;

import Source.StartConsole;
import Tools.CheckingForUpdate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Save extends Commands {

    private final String pathToSave = StartConsole.PATH; // Куда сохранять
    private final List<NoteEntity> listWithNotes = StartConsole.NOTES; // Что сохранять

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated)
            return "Файл сохранён: " + saving(listWithNotes);

        return "Изменений не произошло";
    }

    @Override
    public String perform(String postfix) throws WrongPostfixMethodException {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
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
            fileWriter.write(note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword(false) + "\n\n");
        fileWriter.close();

        CheckingForUpdate.isUpdated = false;

        return true;
    }

}
