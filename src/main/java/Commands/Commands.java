package Commands;

import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;

import java.io.IOException;

public interface Commands {

    // Метод для выполнения команды
    String perform(String postfix) throws IOException, UnknownArgsException, AccessNotFoundException, IncorrectValueException;

}
