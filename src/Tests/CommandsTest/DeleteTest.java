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
        UsefulMethods.changingNameOfAccount(notes, postfixWithName); // Изменение названия сервиса в соответствии с оставшимися у него аккаунтами

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
    void testServiceWithSeveralAccounts() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Vk.com (3-rd account)", "third.account@gmail.com"); note3.setPassword("password_vk_3");
        NoteEntity note4 = new NoteEntity("Vk.com (4-th account)", "fourth.account@gmail.com"); note4.setPassword("password_vk_4");
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);
        notes.add(note4);

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

        NoteEntity noteEntity = UsefulMethods.getAccountFromServiceByLogin(notes, postfixWithName, deletedLogin); // Выбор сервиса по логину
        notes.remove(noteEntity); // Удаление сервиса аналогично как в классе Delete
        UsefulMethods.changingNameOfAccount(notes, postfixWithName); // Изменение названия сервиса в соответствии с оставшимися у него аккаунтами

        // Удалён второй аккаунт
        boolean deletedSecondAcc = notes.contains(note2);
        Assertions.assertFalse(deletedSecondAcc);

        // Изменение нумерации у третьего и четвёртого аккаунтов
        Assertions.assertEquals("Vk.com (1-st account)", note1.getIdService());
        Assertions.assertEquals("Vk.com (2-nd account)", note3.getIdService());
        Assertions.assertEquals("Vk.com (3-rd account)", note4.getIdService());
    }

    @Override
    @Test
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        Delete delete = new Delete(notes);

        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> delete.perform(""));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> delete.perform("arg1_name arg2_andMore"));
        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class, () -> delete.perform("arg1_name"));

        Assertions.assertEquals("Нет параметров", unknownArgsException1.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException2.getMessage());
        Assertions.assertEquals("Сервис не найден", accessNotFoundException.getMessage());
    }

}