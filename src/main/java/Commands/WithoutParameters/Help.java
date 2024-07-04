package Commands.WithoutParameters;

import Commands.Commands;

public class Help implements Commands {

    public Help() {
    }

    @Override
    public String perform(String postfix) {
        return """
                        Get access: -get- ([cb] || + && [show]) + name || + login
                        Get all services: -getall- [] / [date]
                        Add access: -add- [name] [login] [password]
                        Replace access: -replace- [name] [name/login/password] [newString] / [name] [login] [name/login/password] [newString]
                        Delete access: -delete- [name] / [name] [login]
                        Create a backup: -backup- [make]
                        Check important files: -check- [true (with delay)] / [false (without delay)]
                        Copy file to clipboard  windows: -copyfile-
                        Save file: -save-
                        Help: -help-
                        Exit: -exit-
                        """;
    }

}
