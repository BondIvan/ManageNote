package Commands;

import Encrypting.TryDecrypt;
import Entity.NoteEntity;
import Tools.Tools;

public class Get extends Commands {

    /***

     -get- [название]
                      [true] - искать похожие (по первой букве)

     ***/

    private final String postfix;

    public Get(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public String perform() throws Exception {

        String[] args = Tools.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(postfix.length() == 0)
            throw  new UnknownArgsException("Нет параметров");

        if(args.length > 1) { // Проверка на количество параметров в команде

            // Если нужно добавить какие-либо аргументы, расскоментировать if содержащий: "args[1].equalsIgnoreCase("-a")"

            if(args.length > 2) // На данный момент не используется аргументов больше чем обычно
                throw new UnknownArgsException("Параметров больше чем нужно");
            else {
                if(args[1].equalsIgnoreCase("true")) // Получить сервисы начинающиеся на такую же букву
                    return getNote(args, true);
//                if(args[1].equalsIgnoreCase("-a")) // Получить сервисы по введённому логину при 2 аргументе -а
//                    return gettingInfo(getWithLogin(args));
                else
                    throw new UnknownArgsException("Параметров больше чем нужно");
            }
        }
        else
            return getNote(args, false);

    }

    // Получить все данные сервиса из параметров введённой команды
    protected String getNote(String[] args, boolean searchSimilar) throws Exception {

        if(searchSimilar)
            return "Вот все сервисы, которые начинаются на такую же букву: \n" + Tools.getSimilar(args);

        for(NoteEntity note: TestingClass.notes) {

            if (note.getIdService().toLowerCase().contains(args[0].toLowerCase())) {
                if(note.getIdService().contains("account")) {
                    NoteEntity findNote = Tools.getWithLogin(args[0]); // Получить аккаунт сервиса по введённому логину
                    String decPass = TryDecrypt.decrypt(findNote.getPassword()); // Расшифровка пароля

                    return findNote.getIdService() + "\nLogin: " + findNote.getLogin() + "\nPassword: " + decPass;
                }
                else {
                    String decPass = TryDecrypt.decrypt(note.getPassword()); // Расшифровка пароля

                    return note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + decPass;
                }
            }

        }

        return "Такого сервиса нет";
    }


}
