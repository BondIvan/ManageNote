package Commands.WithOrWithoutParameters;


import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.UsefulMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

            if (args.length > 2)
                throw new UnknownArgsException("Параметров больше чем нужно");

            List<NoteEntity> list = new ArrayList<>();
            for(String str: args) {

                if( str.equals("date") )
                    list = getAllByDate();
                if(str.length() == 1)
                    getAllByFirstLetter(list, str);

            }

//            if (args.length > 2)
//                throw new UnknownArgsException("Параметров больше чем нужно");
//            if (!args[0].equals("date"))
//                throw new UnknownArgsException("Неверный параметр");

            System.out.println("-----------------");
            getAllByDate().forEach(System.out::println);
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
        List<NoteEntity> listForShow = new ArrayList<>(listWithNotes);
        UsefulMethods.sortNoteEntityByServiceName(listForShow) // Этот список будет отображаться, чтобы не сортировать основной список
                .forEach(note -> System.out.println(note.getIdService()));
    }

    private List<String> getAllByDate() {
        //listWithNotes.forEach(note -> System.out.println(note.getIdService()));

        return listWithNotes.stream().map(NoteEntity::getIdService).toList();
    }

    private List<String> getAllByDate(List<String> listByFirstLetter) {
        return listByFirstLetter;
    }

    private void getAllByFirstLetter(String letter) {

        List<String> uniqueName = UsefulMethods.getAllUniqueServiceName(listWithNotes);
        uniqueName.stream()
                .filter(note -> note.startsWith(letter))
                .forEach(System.out::println);

    }

    private List<String> getAllByFirstLetter(List<NoteEntity> list, String letter) {

        List<String> uniqueName = UsefulMethods.getAllUniqueServiceName(list);
        return uniqueName.stream()
                .filter(note -> note.startsWith(letter))
                .collect(Collectors.toList());
    }

}
