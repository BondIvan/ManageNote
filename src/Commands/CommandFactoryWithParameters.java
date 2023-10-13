package Commands;

import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactoryWithParameters {

    private final Map<String, Class<? extends CommandsWithParameters>> registeredCommand = new HashMap<>();

    public void registerCommand(String name, Class<? extends CommandsWithParameters> commandClass) {

        registeredCommand.put(name, commandClass);
    }

    public CommandsWithParameters createCommandInstance(String name, List<NoteEntity> list) throws AccessNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<? extends CommandsWithParameters> commandClass = registeredCommand.get(name);

        if(commandClass == null)
            throw new AccessNotFoundException("Команда не найдена");

        Constructor<? extends CommandsWithParameters> constructor = commandClass.getConstructor(List.class); // Задание параметра в конструктор класса

        return constructor.newInstance(list);
    }

}
