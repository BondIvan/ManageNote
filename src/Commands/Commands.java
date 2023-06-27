package Commands;

import java.util.HashSet;
import java.util.Set;

public abstract class Commands {
    private static final String[] allCommands = { "get", "add", "replace", "delete", "help", "exit", "save", "getall", "copyfile" };

    public static boolean isExist(String commandName) {
        Set<String> listCommands = new HashSet<>(Set.of(allCommands));

        return listCommands.contains(commandName);
    }

    public abstract String perform() throws Exception; // Во всех командах это проверка введённых аргументов и выполнение самой команды

}
