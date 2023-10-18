package Commands.WithoutParameters;


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

    //TODO Создал ветку для добавления функции разных сортировок.
    // Сделать сортировку по дате добавления сверху самые старые - снизу новые, то есть ничего не менять.

    @Override
    public String perform(String postfix) throws UnknownArgsException {

        if(!postfix.isEmpty()) {

            String[] args = UsefulMethods.makeArgsTrue(postfix);

            if (args.length > 1)
                throw new UnknownArgsException("Параметров больше чем нужно");
            if (!args[0].equals("date"))
                throw new UnknownArgsException("Неверный параметр");

            System.out.println("-----------------");
            getAllByDate();
        } else {
            System.out.println("-----------------");
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

        UsefulMethods.sortNoteEntityByServiceName(new ArrayList<>(listWithNotes)) // Этот список будет отображаться, чтобы не сортировать основной список
                .forEach(note -> System.out.println(note.getIdService()));
    }

    private void getAllByDate() {

        listWithNotes.forEach(note -> System.out.println(note.getIdService()));
    }

}
