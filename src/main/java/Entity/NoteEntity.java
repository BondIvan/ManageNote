package Entity;

import Encrypting.MyEncrypt.Alphabet.Alphabet;
import Encrypting.MyEncrypt.Alphabet.ViewDecrypt;
import Encrypting.MyEncrypt.Alphabet.ViewEncrypt;
import OptionsExceptions.UnknownArgsException;
import de.huxhorn.sulky.ulid.ULID;

public class NoteEntity {
    private static final ULID ULID = new ULID(); // Генерация уникальных id
    private final String id;
    private String serviceName;
    private String login;
    private String password;

    public NoteEntity() {
        this.id = generateID();
    }

    public NoteEntity(String serviceName, String login) {
        this.id = generateID();
        this.serviceName = serviceName;
        this.login = login;
    }

    public NoteEntity(String serviceName, String id, String login, String password) {
        this.serviceName = serviceName;
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return this.serviceName;
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

    private String generateID() {
        // Возможно стоит добавить проверку, существует ли такой id
        return ULID.nextULID();
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() { //TODO Подумать, нужно ли здесь расшифровывать пароль

        return serviceName + "\nLogin: " + login + "\nPassword: " + getPassword(true);
    }
}
