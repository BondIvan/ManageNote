package OptionsExceptions;

public class CommandNotFoundException extends Exception {

    // Ошибка указывающая на отсутствие команды
    
    public CommandNotFoundException(String message) {
        super(message);
    }

}
