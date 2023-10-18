package Commands.WithoutParameters;

import Commands.Commands;
import Tools.CheckingForUpdate;

import java.io.IOException;

public class Exit implements Commands {

    @Override
    public String perform(String postfix) throws IOException {

        if(CheckingForUpdate.isUpdated) {
            Save save = new Save();
            System.out.println( save.perform(postfix) );
        }

        System.out.println("Выход из приложения");

        System.exit(0);
        return "";
    }

}
