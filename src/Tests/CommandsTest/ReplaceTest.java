package Tests.CommandsTest;

import Commands.WithParameters.Replace;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Tools.UsefulMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReplaceTest implements TestCommands {

    @Test
    void testReplaceServiceName_withoutAccounts() throws UnknownArgsException, IncorrectValueException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);

        Replace replace = new Replace(notes);

        // Изменить название сервиса без аккаунта на сервис с аккаунтами
        String postfix1 = "Telegram.com service Vk.com";
        System.out.println( replace.perform(postfix1) );

        Assertions.assertEquals("Vk.com (3-rd account)", note3.getIdService());

        // Изменить название сервиса без аккаунта этого же сервиса на "такое" же название
        String postfix2 = "logo.com service Logo.com";
        System.out.println( replace.perform(postfix2) );

        Assertions.assertEquals("Logo.com", note4.getIdService());

        // Изменить название сервиса без аккаунтов на сервис также без аккаунтов
        String postfix3 = "Yandex.ru service Logo.com";
        System.out.println( replace.perform(postfix3) );

        Assertions.assertEquals("Logo.com (1-st account)", note4.getIdService());
        Assertions.assertEquals("Logo.com (2-nd account)", note5.getIdService());
    }

    @Test
    void testReplaceServiceName_withAccounts() throws UnknownArgsException, IncorrectValueException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com (1-st account)", "teleg_first.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com (1-st account)", "logo_first.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("logo.com (2-nd account)", "logo_second.account@gmail.com"); note5.setPassword("password_logo_2");
        NoteEntity note6 = new NoteEntity("logo.com (3-rd account)", "logo_third.account@gmail.com"); note6.setPassword("password_logo_3");
        NoteEntity note7 = new NoteEntity("Telegram.com (2-nd account)", "teleg_second.account@gmail.com"); note7.setPassword("password_telg_2");
        NoteEntity note8 = new NoteEntity("Yandex.com (1-st account)", "yandex_first.account@gmail.com"); note8.setPassword("password_yandex_1");
        NoteEntity note9 = new NoteEntity("Yandex.com (2-nd account)", "yandex_second.account@gmail.com"); note9.setPassword("password_yandex_2");
        NoteEntity note10 = new NoteEntity("Yandex.com (3-rd account)", "yandex_third.account@gmail.com"); note10.setPassword("password_yandex_3");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);
        notes.add(note6); notes.add(note7); notes.add(note8); notes.add(note9); notes.add(note10);

        Replace replace = new Replace(notes);

        // Изменить название аккаунта сервиса на название аккаунта другого сервиса
        String[] args1 = "logo.com service Vk.com".split(" "); // Имитация args[]
        NoteEntity workNote1 = UsefulMethods.getAccountFromServiceByLogin(notes, args1[0], "logo_second.account@gmail.com");
        replace.replaceServiceName(workNote1, args1[2]);

        Assertions.assertEquals("Vk.com (3-rd account)", note5.getIdService());
        Assertions.assertEquals("logo.com (2-nd account)", note6.getIdService());

        // Изменить название сервиса с аккаунтами так, чтобы у этого сервиса остался 1 аккаунт
        String[] args2 = "logo.com service Micro.com".split(" ");
        NoteEntity workNote2 = UsefulMethods.getAccountFromServiceByLogin(notes, args2[0], "logo_first.account@gmail.com");
        replace.replaceServiceName(workNote2, args2[2]);

        Assertions.assertEquals("logo.com", note6.getIdService());
        Assertions.assertEquals("Micro.com", note4.getIdService());

        // Передать 1 аккаунт от сервиса с 2 аккаунтами к сервису без аккаунтов
        String[] args3 = "Telegram.com service Micro.com".split(" ");
        NoteEntity workNote3 = UsefulMethods.getAccountFromServiceByLogin(notes, args3[0], "teleg_first.account@gmail.com");
        replace.replaceServiceName(workNote3, args3[2]);

        Assertions.assertEquals("Telegram.com", note7.getIdService());
        Assertions.assertEquals("Micro.com (1-st account)", note3.getIdService());
        Assertions.assertEquals("Micro.com (2-nd account)", note4.getIdService());

        // Изменить регистр букв/ы в названии какого-либо аккаунта
        String[] args4 = "yandex.com service yandex.com".split(" ");
        NoteEntity workNote4 = UsefulMethods.getAccountFromServiceByLogin(notes, args4[0], "yandex_second.account@gmail.com");
        replace.replaceServiceName(workNote4, args4[2]);

        Assertions.assertEquals("Yandex.com (1-st account)", note8.getIdService());
        Assertions.assertEquals("yandex.com (2-nd account)", note9.getIdService());
        Assertions.assertEquals("Yandex.com (3-rd account)", note10.getIdService());
    }

    @Test
    void testReplaceServiceLogin_withoutAccounts() throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com", "vk.account@gmail.com"); note1.setPassword("password_vk");
        notes.add(note1);

        Replace replace = new Replace(notes);

        // Изменить логин у сервиса без аккаунтов
        String postfix = "vk.com login newString";
        System.out.println( replace.perform(postfix) );

        Assertions.assertEquals("newString", note1.getLogin());
    }

    @Test
    void testReplaceServiceLogin_withAccounts() throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        notes.add(note1); notes.add(note2);

        Replace replace = new Replace(notes);

        String[] args1 = "vk.com login newString".split(" ");
        NoteEntity workNote1 = UsefulMethods.getAccountFromServiceByLogin(notes, args1[0], "second.account@gmail.com");
        replace.replaceServiceLogin(workNote1, args1[2]);

        Assertions.assertEquals("newString", note2.getLogin());
    }

    @Test
    void testReplaceServicePassword_withoutAccounts() throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com", "vk.account@gmail.com"); note1.setPassword("password_vk");
        notes.add(note1);

        Replace replace = new Replace(notes);

        // Изменить пароль для сервиса без аккаунтов
        String postfix = "vk.com password newString";
        System.out.println( replace.perform(postfix) );

        Assertions.assertEquals("newString", note1.getPassword(true));
    }

    @Test
    void testReplaceServicePassword_withAccounts() throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        notes.add(note1); notes.add(note2);

        Replace replace = new Replace(notes);

        String[] args1 = "vk.com password newString".split(" ");
        NoteEntity workNote1 = UsefulMethods.getAccountFromServiceByLogin(notes, args1[0], "first.account@gmail.com");
        replace.replaceServicePassword(workNote1, args1[2]);

        Assertions.assertEquals("newString", note1.getPassword(true));
    }

    @Test
    @Override
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        Replace replace = new Replace(notes);

        NoteEntity note1 = new NoteEntity("Vk.com", "first.account@gmail.com");
        NoteEntity note2 = new NoteEntity("Telegram.com", "first.account@gmail.com");
        notes.add(note1);
        notes.add(note2);
        try {
            note1.setPassword("password_vk_1");
            note2.setPassword("password_telegram_2");
        } catch (UnknownArgsException e) {
            System.err.println(e.getMessage());
        }

        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class, () -> replace.perform("arg1_name service arg3_newString"));
        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> replace.perform("arg1_name arg2_typeAndLess"));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> replace.perform("arg1_name login arg3_newString arg4_andMore"));
        Exception unknownArgsException3 = assertThrows(UnknownArgsException.class, () -> replace.perform("Vk.com arg2_wrongType arg3_newString"));
        Exception incorrectValueException1 = assertThrows(IncorrectValueException.class, () -> replace.perform("Vk.com login first.account@gmail.com"));
        Exception incorrectValueException2 = assertThrows(IncorrectValueException.class, () -> replace.perform("Telegram.com service Vk.com"));

        Assertions.assertEquals("Сервис не найден", accessNotFoundException.getMessage());
        Assertions.assertEquals("Параметров меньше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException2.getMessage());
        Assertions.assertEquals("Неизвестный параметр", unknownArgsException3.getMessage());
        Assertions.assertEquals("У этого сервиса такой логин уже существует", incorrectValueException1.getMessage()); //Если менять только логин
        Assertions.assertEquals("Такой логин уже есть", incorrectValueException2.getMessage()); //Если менять только логин
    }
}