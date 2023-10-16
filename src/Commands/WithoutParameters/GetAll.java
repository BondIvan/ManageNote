package Commands.WithoutParameters;


import Commands.Commands;
import Entity.NoteEntity;
import Source.StartConsole;
import Tools.UsefulMethods;

import java.util.ArrayList;
import java.util.List;

public class GetAll implements Commands {

    private final List<NoteEntity> listWithNotes = StartConsole.NOTES;

    @Override
    public String perform(String postfix) throws Exception {

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

}
