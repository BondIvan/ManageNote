package Commands;

import Entity.NoteEntity;

import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import OptionsExceptions.WrongPostfixMethodException;

import Tools.UsefulMethods;
import Tools.CheckingForUpdate;

import java.util.List;
import java.util.Scanner;

public class Delete extends Commands {

    /***

     -delete- [название]

     ***/

    private final List<NoteEntity> listWithNotes;
    public Delete(List<NoteEntity> listWithNotes) {
        this.listWithNotes = listWithNotes;
    }

    @Override
    public String perform() throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

    @Override
    public String perform(String postfix) throws Exception {

        String[] args = UsefulMethods.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if(postfix.length() == 0)
            throw  new UnknownArgsException("Нет параметров");
        if(args.length > 2)
            throw new UnknownArgsException("Параметров больше чем нужно");

        return "Аккаунт удалён: " + deleteNote(args); // Удаление одного из сервисов
    }

    // Удаление сервиса беря название из аргументов команды
    private boolean deleteNote(String[] args) throws Exception {

        String currentServiceName; // Текущий (рассматриваемый сервис)
        for(int i = 0; i < listWithNotes.size(); i++) {
            currentServiceName = listWithNotes.get(i).getIdService(); // Рассматриваемый сервис

            Scanner confirmForDelete = new Scanner(System.in);

            if(currentServiceName.split(" ")[0].equalsIgnoreCase(args[0])) { // Сравнивается первое слово текущего сервиса с требуемым
                if(currentServiceName.contains("account")) { // Удаление сервиса у которого есть несколько аккаунтов
                    NoteEntity needDelete = UsefulMethods.getAccountFromServiceByLogin(listWithNotes, args[0]); // Аккаунт, который выбран как удаляемый

                    System.out.println("Вы уверены, что хотите удалить аккаунт: " + needDelete.getIdService() + " ? (y/n)");
                    if(confirmForDelete.nextLine().equalsIgnoreCase("y")) { // Подтверждение на удаление

                        listWithNotes.remove(needDelete); // Удаление аккаунта сервиса по ведённому логину

                        // После удаления аккаунта проход по всем сервисам, чтобы изменить номер у аккантов сервиса у которого был удалён аккаунт
                        UsefulMethods.changingNameOfAccount(listWithNotes, args[0]);

                        CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                        return true;
                    } else
                        return false;
                }

                // Удаление сервиса у которого нет аккаунтов
                System.out.println("Вы уверены, что хотите удалить сервис: " + listWithNotes.get(i).getIdService() + " ? (y/n)");
                if (confirmForDelete.nextLine().equalsIgnoreCase("y")) { // Подтверждение на удаление
                    listWithNotes.remove(listWithNotes.get(i)); // Удаление сервиса

                    CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                    return true;
                } else
                    return false;

            }

        }

        throw new AccessNotFoundException("Такого сервиса нет");
    }

}
