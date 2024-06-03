package CommandsTest;

import Commands.WithParameters.Delete;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Tools.UsefulMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DeleteTest implements TestCommands {

    @Test
    void testDelete_perform() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru (1-st account)", "yandex1.account@gmail.com"); note5.setPassword("password_yandex_1");
        NoteEntity note6 = new NoteEntity("Yandex.ru (2-nd account)", "yandex2.account@gmail.com"); note6.setPassword("password_yandex_2");
        NoteEntity note7 = new NoteEntity("Yandex.ru (3-rd account)", "yandex3.account@gmail.com"); note7.setPassword("password_yandex_3");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5); notes.add(note6); notes.add(note7);

        Delete delete = new Delete(notes);

        String postfix1 = "telegram.com";
        String actual1 = delete.perform(postfix1);
        boolean isDeleted1 = notes.stream().anyMatch(note -> note.getServiceName().equals("Telegram.com"));
        Assertions.assertFalse(isDeleted1);
        Assertions.assertEquals("Удалено", actual1);

        String postfix2 = "vk.com first.account@gmail.com";
        String actual2 = delete.perform(postfix2);
        boolean isDeleted2 = notes.stream().anyMatch(note -> note.getServiceName().equals("Vk.com (1-st account)"));
        Assertions.assertFalse(isDeleted2);
        Assertions.assertEquals("Удалено", actual2);
        Assertions.assertEquals("Vk.com", note2.getServiceName());

        String postfix3 = "logo.com logo.account@gmail.com";
        String actual3 = delete.perform(postfix3);
        boolean isDeleted3 = notes.stream().anyMatch(note -> note.getServiceName().equals("logo.com"));
        Assertions.assertFalse(isDeleted3);
        Assertions.assertEquals("Удалено", actual3);

        String postfix4 = "yandex.ru";
        String actual4 = delete.perform(postfix4);
        List<NoteEntity> yandexList = notes.stream()
                .filter(note -> note.getServiceName().contains("Yandex.ru"))
                .collect(Collectors.toList());

        Assertions.assertEquals(List.of(note5, note6, note7), yandexList);
        Assertions.assertEquals("Теперь введите команду", actual4);
    }

    @Test
    void testDelete_deleteNoteByLogin() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);

        Delete delete = new Delete(notes);

        String[] args1 = UsefulMethods.makeArgsTrue("vk.com second.account@gmail.com"); // Сервис с аккаунтами
        String[] args2 = UsefulMethods.makeArgsTrue("telegram.com teleg.account@gmail.com"); // Сервис без аккаунтов

        String actual1 = delete.deleteNoteByLogin(args1[0], args1[1]);
        String actual2 = delete.deleteNoteByLogin(args2[0], args2[1]);

        boolean isDeleted1 = notes.stream().anyMatch(note -> note.getServiceName().equals("Vk.com (2-nd account)"));
        boolean isDeleted2 = notes.stream().anyMatch(note -> note.getServiceName().equals("telegram.com"));

        Assertions.assertFalse(isDeleted1);
        Assertions.assertFalse(isDeleted2);

        Assertions.assertEquals("Удалено", actual1);
        Assertions.assertEquals("Удалено", actual2);
    }

    @Test
    void testDelete_deleteNote() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5);

        Delete delete = new Delete(notes);

        String[] args1 = UsefulMethods.makeArgsTrue("vk.com"); // Сервис с аккаунтами
        String[] args2 = UsefulMethods.makeArgsTrue("telegram.com"); // Сервис без аккаунтов

        String actual1 = delete.deleteNote( args1[0] );
        String actual2 = delete.deleteNote( args2[0] );

        boolean isDeleted1 = notes.stream().anyMatch(note -> note.getServiceName().equals("Telegram.com"));

        Assertions.assertEquals("Теперь введите команду", actual1);
        Assertions.assertFalse(isDeleted1);
        Assertions.assertEquals("Удалено", actual2);
    }

    @Override
    @Test
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        Delete delete = new Delete(notes);

        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> delete.perform(""));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> delete.perform("arg1_name arg2_log arg3_andMore"));
        Exception accessNotFoundException1 = assertThrows(AccessNotFoundException.class, () -> delete.perform("arg1_name"));
        Exception accessNotFoundException2 = assertThrows(AccessNotFoundException.class, () -> delete.perform("arg1_name arg_log"));

        Assertions.assertEquals("Нет параметров", unknownArgsException1.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException2.getMessage());
        Assertions.assertEquals("Сервис не найден", accessNotFoundException1.getMessage());
        Assertions.assertEquals("Неправильный логин аккаунта", accessNotFoundException2.getMessage());
    }

}