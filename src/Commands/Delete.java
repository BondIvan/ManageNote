package Commands;

import Entity.NoteEntity;

import Tools.Tools;
import Tools.CheckingForUpdate;

import java.util.Scanner;

public class Delete extends Commands {

    /***

     -delete- [название]

     ***/

    private final String postfix;

    public Delete(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public String perform() throws Exception {

        String[] args = Tools.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        // Если нужно добавить какие-либо аргументы, расскоментировать if содержащий: "args[1].equalsIgnoreCase("-a")"

        if(postfix.length() == 0)
            throw  new UnknownArgsException("Нет параметров");

        if(args.length > 1) {

            if(args.length > 2)
                throw new UnknownArgsException("Параметров больше чем нужно");
//            else {
//                if(args[1].equalsIgnoreCase("-a")) // Удаление одного из аккаунта сервиса по ведённому логину
//                    return deleteNote( getWithLogin(args[0]) );
//            }
//
//            throw new UnknownArgsException("Параметров больше чем нужно (неверный 2-ой аргумент)");
        }

        return deleteNote(args); // Удаление одного из сервисов
    }

    // Удаление сервиса беря название из аргументов команды
    private String deleteNote(String[] args) throws Exception {

        String currentName; // Текущий (рассматриваемый сервис)
        for(int i = 0; i < TestingClass.notes.size(); i++) {
            currentName = TestingClass.notes.get(i).getIdService();

            Scanner confirmForDelete = new Scanner(System.in);

            // Привёл к нижнему регистру, чтобы не зависить от регистра при проверке на содержимое
            if(currentName.toLowerCase().contains(args[0].toLowerCase())) {
                if(currentName.contains("account")) { // Удаление сервиса у которого есть несколько аккаунтов
                    NoteEntity deletedNote = Tools.getWithLogin(args[0]); // Аккаунт, который выбран как удаляемый

                    System.out.println("Вы уверены, что хотите удалить аккаунт: " + deletedNote.getIdService() + " ? (y/n)");
                    if(confirmForDelete.nextLine().equalsIgnoreCase("y")) { // Подтверждение на удаление

                        TestingClass.notes.remove(deletedNote); // Удаление аккаунта сервиса по ведённому логину

                        // После удаления аккаунта проход по всем сервисам, чтобы изменить номер у аккантов сервиса у которого был удалён аккаунт
                        Tools.changingNameOfAccount(args[0]);

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return "Аккаунт удалён";
                    }
                    else
                        return "Аккаунт удалён не был";

                }  else { // Удаление сервиса у которого нет аккаунтов
                    NoteEntity deletedNote= TestingClass.notes.get(i); // Сервис, который выбран как удаляемый (через переменную, для лучшей читаемости)

                    System.out.println("Вы уверены, что хотите удалить аккаунт: " + deletedNote.getIdService() + " ? (y/n)");
                    if(confirmForDelete.nextLine().equalsIgnoreCase("y")) { // Подтверждение на удаление
                        TestingClass.notes.remove(deletedNote); // Удаление сервиса

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return "Сервис удалён";
                    }
                    else
                        return "Сервис удалён не был";
                }
            }

        }

        return "Такого сервиса нет";
    }

}
