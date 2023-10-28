package OptionsExceptions;

public class IncorrectValueException extends Exception {

    // Ошибка указывающая на конфликты с изменяемыми данными

    public IncorrectValueException(String message) {
        super(message);
    }

}
