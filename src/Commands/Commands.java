package Commands;

public interface Commands {

    // Метод для выполнения команды
    String perform(String postfix) throws Exception;

}
