package Commands.WithoutParameters;

import Commands.Commands;

public class Help implements Commands {

    @Override
    public String perform(String postfix) {
        return """
                        For get access: -get- [название]  [true] - получить похожие
                        For get names of all services: -getall-
                        For add new access: -add- [название] [login] [password]
                        Replace access: -replace- [название] [service/login/password] [новая строка]
                        Delete access: -delete- [название]
                        Save file: -save-
                        Copy file to clipboard  windows: -copyfile-
                        Help: -help-
                        Exit: -exit-
                        """;
    }

}
