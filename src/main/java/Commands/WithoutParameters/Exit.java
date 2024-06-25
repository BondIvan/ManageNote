package Commands.WithoutParameters;

import Commands.Commands;
import Tools.CheckingForUpdate;

public class Exit implements Commands {

    public Exit() {
    }

    @Override
    public String perform(String postfix) {

        if(CheckingForUpdate.isUpdated) {
            Save save = new Save();
            System.out.println( save.perform(postfix) );
        }

        System.exit(0);
        return "";
    }

}
