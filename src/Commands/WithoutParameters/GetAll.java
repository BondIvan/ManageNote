package Commands.WithoutParameters;


import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.WrongPostfixMethodException;
import Tools.UsefulMethods;

import java.util.ArrayList;
import java.util.List;

public class GetAll extends Commands {

    private final List<NoteEntity> listWithNotes;

    public GetAll(List<NoteEntity> listWithNotes) {
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws Exception {

        System.out.println("-----------------");

        List<NoteEntity> sortListWithNotes = new ArrayList<>(listWithNotes); // Этот список будет отображаться, чтобы не сортировать основной список

        UsefulMethods.sortNoteEntityByServiceName(sortListWithNotes)
                .forEach(note -> System.out.println(note.getIdService()));

        return """
                -----------------
                        ^
                        |
                Это был последний
                """;
    }

    @Override
    public String perform(String postfix) throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

}
