package Commands;

import Encrypting.TestFormula;
import Entity.NoteEntity;

import Tools.UsefulMethods;
import Tools.CheckingForUpdate;

import java.util.List;

public class Replace extends Commands {

    /***

     -replace- [название] [название изменяемого] [новая строка]

     ***/

    private final String postfix;

    public Replace(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public String perform() throws Exception {

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length < 3)
            throw new UnknownArgsException("Параметров меньше чем нужно");
        if(args.length > 3)
            throw new UnknownArgsException("Параметров больше чем нужно");

        return replaceNote(args, args[1]);
    }

    //TODO Текущая конструкция аргументов команды под сомнением

    // System.out.println(note.getIdService() + " -> " + args[2] + "\nLogin: " + note.getLogin() + "\n" + note.getPassword());
    // System.out.println(note.getIdService() + "\n" + note.getLogin() + " -> " + args[2] + "\n" + note.getPassword());
    // System.out.println(note.getIdService() + "\n" + note.getLogin() + "\n" + note.getPassword() + " -> " + args[2]);

    // Scanner confirm = new Scanner(System.in);
    // System.out.println("Вы уверены что хотите изменить запись? (y/n)");


    private String replaceNote(String[] args, String nameOfParameter) throws Exception {

        //TODO Возможно есть лучшее решение, не через такое количество if

        // String[] nameOfParam = { "service",  "login", "password" };

        for (NoteEntity note : TestingClass.notes) {

            String currentServiceName = note.getIdService();
            if (currentServiceName.split(" ")[0].equalsIgnoreCase(args[0])) { // Сравнивается первое слово текущего сервиса с требуемым
                if (currentServiceName.contains("account")) {

                    NoteEntity findNote = UsefulMethods.getWithLogin(args[0]);

                    if (nameOfParameter.equalsIgnoreCase("service")) {

                        List<NoteEntity> allAccountsOfService = UsefulMethods.getAllAccounts(args[2]);
                        if(allAccountsOfService.isEmpty())
                            findNote.setIdService(args[2]);
                        else
                            return "Сервис " + allAccountsOfService.get(0).getIdService() + " уже существует";

                    }
                    else if (nameOfParameter.equalsIgnoreCase("login")) {
                        findNote.setLogin(args[2]);
                    }
                    else if (nameOfParameter.equalsIgnoreCase("password")) {
                        String encrptPass = TestFormula.analysisString(args[2]);

                        findNote.setPassword(encrptPass);
                    } else
                        throw new UnknownArgsException("Неизвестный параметр изменения"); // Exception если в параметрах указано неизвестное изменение (Exp: replace id)

                    CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                    return "Значение измененно, вот результат:\n" + findNote.getIdService() + "\nLogin: " + findNote.getLogin() + "\nPassword: " + findNote.getPassword();
                } else { // Сервис с 1 аккаунтом

                    if (nameOfParameter.equalsIgnoreCase("service")) {
                        List<NoteEntity> allAccountsOfService = UsefulMethods.getAllAccounts(args[2]);
                        if(allAccountsOfService.isEmpty())
                            note.setIdService(args[2]);
                        else
                            return "Сервис " + allAccountsOfService.get(0).getIdService() + " уже существует";

                    } else if (nameOfParameter.equalsIgnoreCase("login")) {
                        note.setLogin(args[2]);
                    } else if (nameOfParameter.equalsIgnoreCase("password")) {
                        String encrptPass = TestFormula.analysisString(args[2]);

                        note.setPassword(encrptPass);
                    } else
                        throw new UnknownArgsException("Неизвестный параметр изменения"); // Exception если в параметрах указано неизвестное изменение (Exp: replace id)

                    CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                    return "Значение измененно, вот результат:\n" + note.getIdService() + "\nLogin: " + note.getLogin() + "\nPassword: " + note.getPassword();
                }
            }

        }

            return "Такого сервиса нет";
        }

}
