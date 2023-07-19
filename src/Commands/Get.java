package Commands;

import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import OptionsExceptions.WrongPostfixMethodException;
import Tools.UsefulMethods;

import java.util.List;

public class Get extends Commands {

    /***

     -get- [название]
                      [true] - искать похожие (по первой букве)

     ***/

    private final List<NoteEntity> listWithNotes;

    public Get(List<NoteEntity> list) {
        this.listWithNotes = list;
    }

    @Override
    public String perform() throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

    @Override
    public String perform(String postfix) throws Exception {

        String[] args = UsefulMethods.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(args.length == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 1)
            throw new UnknownArgsException("Параметров больше чем нужно");
        else
            return getNote(args).toString();

    }

    // Получить все данные сервиса из параметров введённой команды
    protected NoteEntity getNote(String[] args) throws Exception {

        for(NoteEntity note: listWithNotes) {

            String currentServiceName = note.getIdService();
            if (currentServiceName.split(" ")[0].equalsIgnoreCase(args[0])) { // Сравнивается первое слово текущего сервиса с требуемым
                if(currentServiceName.contains("account")) { // Содержит ли сервис аккаунты

                    return UsefulMethods.getWithLogin(listWithNotes, args[0]); // Получить аккаунт сервиса по введённому логину
                }
                else {
                    return note;
                }
            }

        }

        throw new AccessNotFoundException("Такого сервиса нет");
    }


}
