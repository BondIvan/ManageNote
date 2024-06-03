package CommandsTest;

import Commands.WithParameters.Get;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GetTest implements TestCommands {

    /***
     *
     * Проверка всех ситуаций в методе perform в классе Get.class
     *
     ***/

    @Test
    void testGet_perform() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);

        Dictionaries dictionaries = new Dictionaries();
        dictionaries.fillingDictionaries(notes);

        Get get = new Get(notes);

        String postfix1 = "telegram.com";
        String postfix2  = "telegram.com teleg.account@gmail.com";
        String postfix3 = "vk.com";
        String postfix4 = "vk.com first.account@gmail.com";

        String actual1 = get.perform(postfix1);
        String actual2 = get.perform(postfix2);
        String actual3 = get.perform(postfix3);
        String actual4 = get.perform(postfix4);

        String expected1 = """
                --------------------------------------
                Telegram.com
                Login: teleg.account@gmail.com
                Password: password_teleg_1
                --------------------------------------""";
        String expected2 = """
                --------------------------------------
                Telegram.com
                Login: teleg.account@gmail.com
                Password: password_teleg_1
                --------------------------------------""";
        String expected3 = """
                --------------------------------------
                Vk.com (1-st account)
                Login: first.account@gmail.com
                Password: password_vk_1
                --------------------------------------
                Vk.com (2-nd account)
                Login: second.account@gmail.com
                Password: password_vk_2
                --------------------------------------""";
        String expected4 = """
                --------------------------------------
                Vk.com (1-st account)
                Login: first.account@gmail.com
                Password: password_vk_1
                --------------------------------------""";

        // Проверка совпадает ли результат метода perform с ожидаемым
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
        Assertions.assertEquals(expected3, actual3);
        Assertions.assertEquals(expected4, actual4);
    }

    @Test
    void testGet_getListWithNotes() throws AccessNotFoundException, UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);

        Dictionaries dictionaries = new Dictionaries();
        dictionaries.fillingDictionaries(notes);

        Get get = new Get(notes);

        String[] args1 = UsefulMethods.makeArgsTrue("vk.com"); // Сервис с аккаунтами
        String[] args2 = UsefulMethods.makeArgsTrue("telegram.com"); // Сервис без аккаунтов

        List<NoteEntity> actual1 = get.getListWithNotes( args1[0] );
        List<NoteEntity> actual2 = get.getListWithNotes( args2[0] );

        // Получение сервиса с аккаунтами и без
        Assertions.assertEquals(List.of(note1, note2), actual1);
        Assertions.assertEquals(List.of(note3), actual2);
    }

    @Test
    void testGet_getNoteByLogin() throws AccessNotFoundException, UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);

        Dictionaries dictionaries = new Dictionaries();
        dictionaries.fillingDictionaries(notes);

        Get get = new Get(notes);

        // Специально с нижним регистром
        String[] args1 = UsefulMethods.makeArgsTrue("telegram.com teleg.account@gmail.com");

        NoteEntity actual1 = get.getNoteByLogin( args1[0], args1[1] );

        // Получение сервиса без аккаунтов
        Assertions.assertEquals(note3, actual1);
    }

    // Проверка аргументов
    @Override
    @Test
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        try {
            NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com");
            note1.setPassword("password_vk_1");
            NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com");
            note2.setPassword("password_vk_2");
            NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com");
            note3.setPassword("password_teleg_1");
            NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com");
            note4.setPassword("password_logo_1");
            NoteEntity note5 = new NoteEntity("Yandex.ru (1-st account)", "yandex1.account@gmail.com");
            note5.setPassword("password_yandex_1");
            NoteEntity note6 = new NoteEntity("Yandex.ru (2-nd account)", "yandex2.account@gmail.com");
            note6.setPassword("password_yandex_2");
            NoteEntity note7 = new NoteEntity("Yandex.ru (3-rd account)", "yandex3.account@gmail.com");
            note7.setPassword("password_yandex_3");

            notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5); notes.add(note6); notes.add(note7);
        } catch (UnknownArgsException e) {
            System.out.println("Ошибка (я): " + e.getMessage());
        }
        
        Get get = new Get(notes);

        Exception accessNotFoundException1 = assertThrows(AccessNotFoundException.class, () -> get.perform("DontExist"));
        Exception accessNotFoundException2 = assertThrows(AccessNotFoundException.class, () -> get.perform("Telegram.com wrongLogin"));
        Exception unknownArgsException1 = assertThrows(OptionsExceptions.UnknownArgsException.class, () -> get.perform("Telegram.com arg2 arg3"));
        Exception unknownArgsException2 = assertThrows(OptionsExceptions.UnknownArgsException.class, () -> get.perform(""));

        Assertions.assertTrue(accessNotFoundException1.getMessage().contains("Сервис не найден"));
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Нет параметров", unknownArgsException2.getMessage());
        Assertions.assertEquals("Неправильный логин аккаунта", accessNotFoundException2.getMessage());
    }

}