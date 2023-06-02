package Entity;

public class NoteEntity {
    private String idService; // id -> name
    private String login;
    private String password;

    public NoteEntity() {

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
 //TODO Для методов получения (get) возвращаемое значение обрезать, чтобы возвращалось только значение, без Login:/Password:
    public void setLogin(String login) {
        // login = "Without -> Login: ..."
        this.login = login;
    }
    public String getLogin() {
        return this.login;
    }

    public void setPassword(String password) {
        // password = "Without -> Password: ..."
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        //TODO Подумать, нужно ли здесь расшифровывать пароль
        return idService + "\n" + login + "\n" + password;
    }
}
