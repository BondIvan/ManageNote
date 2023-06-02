package Commands;

public class Help extends Commands {

    /***

     -help-

     ***/

    @Override
    public String perform() {
        return """
                        For get access: -get- [название]  [true] - получить похожие
                        
                        For get names of all services: -getall-
                                                                               
                        For add new access: -add- [название] [login] [password]
                        
                        Replace access: -replace- [название] [service/login/password] [новая строка]
                        
                        Delete access: -delete- [название]
                        
                        Save file: -save-
                        
                        Help: -help-
                        
                        Exit: -exit-
                        """;
    }
}
