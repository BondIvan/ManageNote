package Commands;


import OptionsExceptions.WrongPostfixMethodException;
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

    @Override
    public String perform(String postfix) throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

}
