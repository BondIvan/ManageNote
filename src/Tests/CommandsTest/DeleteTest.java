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

    //TODO
    // У сервиса несколько аккаунтов

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
    void testServiceWithOneAccount() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        notes.add(note1);
        notes.add(note2);

        // В автоматических тестах не принято использовать какое-то взаимодействие с пользователем.
        // Поэтому в этом тесте используется такой же порядок действий как и в классе Delete.
        // Грубо говоря, здесь тестируются методы из класса UsefulMethods

        // Получение нужного аккаунта сервиса по логину
        String postfix = "vk.com";
        String deletedLogin = "second.account@gmail.com";
        NoteEntity deletedNote = UsefulMethods.getAccountFromServiceByLogin(notes, postfix, deletedLogin);
        notes.remove(deletedNote);

        // Изменение названия сервиса в соответствии с оставшимися у него аккаунтами
        UsefulMethods.changingNameOfAccount(notes, postfix);

        // Удалён второй аккаунт
        boolean deletedSecondAcc = notes.stream()
                        .anyMatch(note -> note.getIdService().equalsIgnoreCase("Vk.com (2-nd account)"));

        boolean remainingAcc = notes.stream()
                        .anyMatch(note -> note.getIdService().equalsIgnoreCase("vk.com"));

        Assertions.assertFalse(deletedSecondAcc);
        Assertions.assertTrue(remainingAcc);
    }

    @Test
    void testServiceWithSeveralAccounts() {

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