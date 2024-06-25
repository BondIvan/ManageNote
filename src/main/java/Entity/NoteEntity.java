package Entity;

import Encrypting.Security.Encryption_AES.AES_GCM;
import OptionsExceptions.UnknownArgsException;
import de.huxhorn.sulky.ulid.ULID;

import java.security.KeyStoreException;

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
        
        try {
            AES_GCM aesGcm = new AES_GCM();
            this.password = aesGcm.encrypt(password, this.id); // password = "Without -> Password: ..."
        } catch (Exception e) {
            System.out.println("Не удалось задать пароль, тип ошибки - " + e.getMessage());
        }

    }
    public String getPassword(boolean needDecrypt) throws KeyStoreException { // needDecrypt - будет говорить, нужно ли расшифровать пароль

        try {
            AES_GCM aesGcm = new AES_GCM();

            return needDecrypt ? aesGcm.decrypt(this.password, this.id) : this.password;
        } catch (Exception e) {
            throw new KeyStoreException("Не удалось получить пароль.\nОшибка получения доступа к защищённому хранилищу.\n" + e.getMessage());
        }
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

        try {
            return serviceName + "\nLogin: " + login + "\nPassword: " + getPassword(true);
        } catch (KeyStoreException e) {
            return "Ошибка получения доступа к защищённому хранилищу.\n" + e.getMessage();
        }
    }
}
