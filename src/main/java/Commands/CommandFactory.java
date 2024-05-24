package Commands;

import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.CommandNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private final Map<String, Class<? extends Commands>> registeredCommand = new HashMap<>();

    public void registerCommand(String name, Class<? extends Commands> commandClass) {

        registeredCommand.put(name, commandClass);
    }

    public Commands getCommand(String name) throws CommandNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {

        Class<? extends Commands> commandClass = registeredCommand.get(name);

        if(commandClass == null)
//            throw new CommandNotFoundException("Такой команды нет");
            return null;

        return commandClass.getDeclaredConstructor().newInstance();
    }

}
