package Commands.WithoutParameters;

import Commands.Commands;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Telegram.Commands.SendFile;
import Tools.UsefulMethods;

import java.io.IOException;

public class PushFile implements Commands {

    private final String pathToFile;

    public PushFile() {
        this.pathToFile = StartConsole.PATH;
    }

    @Override
    public String perform(String postfix) throws IOException, UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length > 1)
            throw new UnknownArgsException("Параметров больше чем нужно");

        return pushToBot();
    }

    private String pushToBot() throws IOException {

//        String path =

        return "Файл отправлен";
    }

}
