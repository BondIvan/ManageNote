package OptionsExceptions;

public class WrongPostfixMethodException extends Exception {

    // Ошибка, если в команде вызывается метод perform с неправильными параметрами
    public WrongPostfixMethodException(String message) {
        super(message);
    }

}
