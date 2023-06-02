package Commands;

public class GetAll extends Commands {

    /***

     -getall-

     ***/

    @Override
    public String perform() throws Exception {

        getNamesOfAllServices();

        return """
                -----------------
                        ^
                        |
                Это был последний
                """;
    }

    public void getNamesOfAllServices() {

        TestingClass.notes
                .forEach(note -> System.out.println(note.getIdService()));

    }
}
