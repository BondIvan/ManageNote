package Entity;

import Encrypting.Alphabet.Alphabet;
import Encrypting.ViewDecrypt;
import Encrypting.ViewEncrypt;
import OptionsExceptions.UnknownArgsException;

public class NoteEntity {
    private String idService; // id -> name
    private String login;
    private String password;

    public NoteEntity() {

    }

    public NoteEntity(String idService, String login) {
        this.idService = idService; // id -> name
        this.login = login;
    }

    public NoteEntity(String idService, String login, String password) {
        this.idService = idService; // id -> name
        this.login = login;
        this.password = password;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }
    public String getIdService() {
        return this.idService;
    }

    public void setLogin(String login) {
        this.login = login; // login = "Without -> Login: ..."
    }
    public String getLogin() {
        return this.login;
    }

    public void setPassword(String password) throws UnknownArgsException { //TODO Подумать, насколько это разумно

        if(password.contains(".")) // Символ '.' используется для шифрования и расшифрования
            throw new UnknownArgsException("В пароле не должен содержаться символ '.'");

        ViewEncrypt viewEncrypt = new ViewEncrypt(Alphabet.getAlpha());
        this.password = viewEncrypt.encrypting(password); // password = "Without -> Password: ..."
    }
    public String getPassword(boolean needDecrypt) { // needDecrypt - будет говорить, нужно ли расшифровать пароль

        ViewDecrypt viewDecrypt = new ViewDecrypt(Alphabet.getAlpha());

        return needDecrypt ? viewDecrypt.decrypt(this.password) : this.password;
    }

    @Override
    public String toString() { //TODO Подумать, нужно ли здесь расшифровывать пароль

        return idService + "\nLogin: " + login + "\nPassword: " + getPassword(true); // true - говорит, что нужно расщифровать пароль
    }
}
