package Entity;

import Encrypting.MyEncrypt.Alphabet.Alphabet;
import Encrypting.MyEncrypt.Alphabet.ViewDecrypt;
import Encrypting.MyEncrypt.Alphabet.ViewEncrypt;
import Encrypting.Security.Encryption_AES.AES_GCM;
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

    @Override
    public String toString() { //TODO Подумать, нужно ли здесь расшифровывать пароль

        return idService + "\nLogin: " + login + "\nPassword: " + getPassword(true);
    }
}
