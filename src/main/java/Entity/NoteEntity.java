package Entity;

import Encrypting.MyEncrypt.Alphabet.Alphabet;
import Encrypting.MyEncrypt.Alphabet.ViewDecrypt;
import Encrypting.MyEncrypt.Alphabet.ViewEncrypt;
import Encrypting.Security.Encryption_AES.AES_GCM;
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

    public void setPassword(String password) throws UnknownArgsException {

        //TODO Посмотреть корректно ли будет работать замена пароля через команду replace
        
        try {
            AES_GCM aesGcm = new AES_GCM();
            this.password = aesGcm.encrypt(password, this.idService); // password = "Without -> Password: ..."
        } catch (Exception e) {
            System.out.println("Не удалось задать пароль, тип ошибки - " + e.getMessage());
        }

    }
    public String getPassword(boolean needDecrypt) { // needDecrypt - будет говорить, нужно ли расшифровать пароль

        try {
            AES_GCM aesGcm = new AES_GCM();

            return needDecrypt ? aesGcm.decrypt(this.password, this.idService) : this.password;
        } catch (Exception e) {
            System.out.println("Не удалось вывести пароль, тип ошибки - " + e.getMessage());
        }

        return null;
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
