package OptionsExceptions;

public class AccessNotFoundException extends Exception {

    // Ошибка указывающая на отсутствие сервиса

    public AccessNotFoundException(String message) {
        super(message);
    }

}
