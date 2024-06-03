package Commands.WithOrWithoutParameters;


import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.UsefulMethods;

import java.util.ArrayList;
import java.util.List;

public class GetAll implements Commands {

    private final List<NoteEntity> listWithNotes;

    public GetAll() {
        this.listWithNotes = StartConsole.NOTES;
    }

    public GetAll(List<NoteEntity> notes) {
        this.listWithNotes = notes;

    }

    @Override
    public String perform(String postfix) throws UnknownArgsException {

        if(!postfix.isEmpty()) {

            String[] args = UsefulMethods.makeArgsTrue(postfix);

            if (args.length > 1)
                throw new UnknownArgsException("Параметров больше чем нужно");
            if (!args[0].equals("date"))
                throw new UnknownArgsException("Неверный параметр");

            getAllByDate();
        } else {
            getAllByAlphabet();
        }

        return """
                -----------------
                        ^
                        |
                Это был последний
                """;
    }

    private void getAllByAlphabet() {
        List<NoteEntity> listForShow = new ArrayList<>(listWithNotes);
        System.out.println("-----------------");
        UsefulMethods.sortNoteEntityByServiceName(listForShow) // Этот список будет отображаться, чтобы не сортировать основной список
                .forEach(note -> System.out.println(note.getServiceName()));
    }

    private void getAllByDate() {
        System.out.println("-----------------");
        listWithNotes.forEach(note -> System.out.println(note.getServiceName()));
    }

}
