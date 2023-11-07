package Commands.WithoutParameters;

import Commands.Commands;

public class Help implements Commands {

    public Help() {
    }

    @Override
    public String perform(String postfix) {
        return """
                        Get access: -get- [название]
                        Get all services: -getall- [] / [date]
                        Add new access: -add- [название] [login] [password]
                        Replace access: -replace- [название] [service/login/password] [новая строка]
                        Delete access: -delete- [название]
                        Save file: -save-
                        Copy file to clipboard  windows: -copyfile-
                        Help: -help-
                        Exit: -exit-
                        """;
    }

}
