package Commands;

import Encrypting.TestFormula;
import Entity.NoteEntity;

import Tools.Tools;
import Tools.CheckingForUpdate;

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

        String[] args = Tools.makeArgsTrue(postfix);

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

            if (note.getIdService().toLowerCase().contains(args[0].toLowerCase())) {
                if (note.getIdService().contains("account")) {

                    NoteEntity findNote = Tools.getWithLogin(args[0]);

                    if (nameOfParameter.equalsIgnoreCase("service")) {
                        findNote.setIdService(args[2]);
                    }
                    else if (nameOfParameter.equalsIgnoreCase("login")) {
                        findNote.setLogin("Login: " + args[2]);
                    }
                    else if (nameOfParameter.equalsIgnoreCase("password")) {
                        String encrptPass = TestFormula.analysisString(args[2]);

                        findNote.setPassword("Password: " + encrptPass);
                    } else
                        throw new UnknownArgsException("Неизвестный параметр изменения"); // Exception если в параметрах указано неизвестное изменение (Exp: replace id)

                    CheckingForUpdate.isUpdated = true; // Информация, что данные были изменены

                    return "Значение измененно, вот результат:\n" + findNote.getIdService() + "\nLogin: " + findNote.getLogin() + "\nPassword: " + findNote.getPassword();
                } else { // Сервис с 1 аккаунтом

                    if (nameOfParameter.equalsIgnoreCase("service")) {
                        note.setIdService(args[2]);
                    } else if (nameOfParameter.equalsIgnoreCase("login")) {
                        note.setLogin("Login: " + args[2]);
                    } else if (nameOfParameter.equalsIgnoreCase("password")) {
                        String encrptPass = TestFormula.analysisString(args[2]);

                        note.setPassword("Password: " + encrptPass);
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
