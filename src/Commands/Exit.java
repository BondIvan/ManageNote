package Commands;

import OptionsExceptions.WrongPostfixMethodException;
import Source.StartConsole;
import Tools.CheckingForUpdate;

public class Exit extends Commands {

// TODO Разобраться с этой командой
    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated) {
            Commands save = new Save(StartConsole.PATH_TEST);
            System.out.println( save.perform() );
        }

        System.out.println("Exiting the application");

        System.exit(0);
        return "";
    }

    @Override
    public String perform(String postfix) throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }
}
