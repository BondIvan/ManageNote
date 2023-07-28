package Commands;

import Entity.NoteEntity;

import java.util.HashSet;
import java.util.Set;

public abstract class Commands {

    //TODO Не знаю как избежать нарушения принципа interface segregation,
    // поэтому в каждом классе есть метод, который возвращает null (метод perform() перегружен)

    private static final String[] allCommands = { "get", "add", "replace", "delete", "help", "exit", "save", "getall", "copyfile" };

    public static boolean isExist(String commandName) {
        Set<String> listCommands = new HashSet<>(Set.of(allCommands));

        return listCommands.contains(commandName);
    }

    // Во всех командах - это выполнение самой команды
    public abstract String perform() throws Exception;

    // Во всех командах это проверка введённых аргументов и выполнение самой команды
    // В параметре должен быть только postfix введённой команды
    public abstract String perform(String postfix) throws Exception;

}
