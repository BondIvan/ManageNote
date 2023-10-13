package Commands.WithoutParameters;

import Commands.CommandsWithoutParameters;

public class Help extends CommandsWithoutParameters {

    @Override
    public String perform() {
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
