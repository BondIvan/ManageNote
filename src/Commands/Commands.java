package Commands;

public abstract class Commands {
    private static final String[] allCommand = { "get", "add", "replace", "delete", "help", "save", "getall" };

    public static boolean isExist(String commandName) {

        for(String command: allCommand) { // Проверка на наличие команды
            if(command.equals(commandName))
                return true;
        }

        return false;
    }

    public abstract String perform() throws Exception; // Во всех командах это проверка введённых аргументов и выполнение самой команды


    //TODO написать пример, как должны выглядеть поля в файле, чтобы они правильно считывались



}
