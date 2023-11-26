package Tests.CommandsTest;

import Commands.WithParameters.Delete;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Tools.UsefulMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DeleteTest implements TestCommands {

    @Test
    void testServiceWithoutAccount() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note = new NoteEntity("Vk.com", "first.account@gmail.com"); note.setPassword("password_vk_1");
        notes.add(note);

        Delete delete = new Delete(notes);
        System.out.println( delete.perform("Vk.com") );

        boolean isDeleted = notes.stream()
                .anyMatch(note1 -> note1.getIdService().equalsIgnoreCase("Vk.com"));

        Assertions.assertFalse(isDeleted);
    }

    @Test
    void testServiceWithTwoAccount() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        notes.add(note1);
        notes.add(note2);

        String postfixWithName = "vk.com";

        // В автоматических тестах не принято использовать какое-то взаимодействие с пользователем.
        // Поэтому в этом тесте используется такой же порядок действий как и в классе Delete.
        // Грубо говоря, здесь тестируются методы из класса UsefulMethods
        // TODO В будещем нужно будет изменить этот тест

        String wrongLogin = "wrong.login@gmail.com";
        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class,
                () -> UsefulMethods.getAccountFromServiceByLogin(notes, postfixWithName, wrongLogin));

        // Проверка на ошибку неверного ввода логина аккаунта
        Assertions.assertEquals("Такой записи нет", accessNotFoundException.getMessage());

        String deletedLogin = "second.account@gmail.com";
        NoteEntity deletedNote = UsefulMethods.getAccountFromServiceByLogin(notes, postfixWithName, deletedLogin); // Выбор сервиса по логину
        notes.remove(deletedNote); // Удаление сервиса аналогично как в классе Delete
        UsefulMethods.changingNameWhenRemove(notes, postfixWithName); // Изменение названия сервиса в соответствии с оставшимися у него аккаунтами

        // Удалён второй аккаунт
        boolean deletedSecondAcc = notes.contains(note2);
        Assertions.assertFalse(deletedSecondAcc);

        // У оставшегося сервиса удалён номер ("1-st account")
        boolean remainingAcc = notes.stream()
                        .anyMatch(note -> note.getIdService().equalsIgnoreCase("vk.com"));

        Assertions.assertEquals("Vk.com", note1.getIdService());
        Assertions.assertTrue(remainingAcc);
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

        String postfix1 = "vk.com";
        String postfix3 = "telegram.com";
        String postfix4 = "logo.com logo.account@gmail.com";

        String actual1 = delete.deleteNote(UsefulMethods.makeArgsTrue(postfix1));

        Assertions.assertEquals("Теперь введите команду", actual1);

        String message3 = delete.deleteNote(UsefulMethods.makeArgsTrue(postfix3));
        String message4 = delete.deleteNote(UsefulMethods.makeArgsTrue(postfix4));

        boolean doesntEsist1 = notes.stream().anyMatch(note -> note.getIdService().equals("Telegram.com"));
        boolean doesntEsist2 = notes.stream().anyMatch(note -> note.getIdService().equals("logo.com"));

        Assertions.assertFalse(doesntEsist1, message3);
        Assertions.assertFalse(doesntEsist2, message3);
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