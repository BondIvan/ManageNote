package Commands;


import Tools.UsefulMethods;

public class GetAll extends Commands {

    /***

     -getall-

     ***/

    @Override
    public String perform() throws Exception {

        UsefulMethods.sortNoteEntityByServiceName(TestingClass.notes)
                .forEach(note -> System.out.println(note.getIdService()));

        return """
                -----------------
                        ^
                        |
                Это был последний
                """;
    }

}
