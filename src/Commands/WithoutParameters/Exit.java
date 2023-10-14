package Commands.WithoutParameters;

import Commands.Commands;
import OptionsExceptions.WrongPostfixMethodException;
import Tools.CheckingForUpdate;

public class Exit extends Commands {

    @Override
    public String perform() throws Exception {

        if(CheckingForUpdate.isUpdated) {
            Commands save = new Save();
            System.out.println( save.perform() );
        }

        System.out.println("Выход из приложения");

        System.exit(0);
        return "";
    }

    @Override
    public String perform(String postfix) throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }
}
