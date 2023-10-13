package Commands;

import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactoryWithoutParameters {

    private final Map<String, Class<? extends CommandsWithoutParameters>> registeredCommand = new HashMap<>();

    public void registerCommand(String name, Class<? extends CommandsWithoutParameters> commandClass) {

        registeredCommand.put(name, commandClass);
    }

    public CommandsWithoutParameters createCommandInstance(String name, List<NoteEntity> list) throws AccessNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<? extends CommandsWithoutParameters> commandClass = registeredCommand.get(name);

        if(commandClass == null)
            throw new AccessNotFoundException("Команда не найдена");

        Constructor<? extends CommandsWithoutParameters> constructor = commandClass.getConstructor(List.class); // Задание параметра в конструктор класса

        return constructor.newInstance(list);
    }

}
