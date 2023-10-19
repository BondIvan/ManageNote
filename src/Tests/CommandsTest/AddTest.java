package Tests.CommandsTest;

import Commands.WithParameters.Add;
import Entity.NoteEntity;
import OptionsExceptions.UnknownArgsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AddTest {

    //TODO
    // 1.

    @Test
    void testAddPerform()  {




    }

    // Проверка добавления второго аккаунта к уже существующему сервису
    @Test
    public void testForNewAccountForAlreadyExistService() throws UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note = new NoteEntity("Vk.com", "vano.525.2552@gmail.com"); note.setPassword("password_vk_1");
        notes.add(note);

        Add add = new Add(notes);
        String addPostfix1 = "Vk.com 375257291200 password_vk_2";
        System.out.println(add.perform(addPostfix1)); // Добавление нового аккаунта к сервису Vk.com

        boolean existForFirstAccount = notes.stream()
                .anyMatch(note1 -> note1.getIdService().equalsIgnoreCase("Vk.com (1-st account)"));
        boolean existForSecondAccount = notes.stream()
                .anyMatch(note1 -> note1.getIdService().equalsIgnoreCase("Vk.com (2-nd account)"));

        Assertions.assertTrue(existForFirstAccount);
        Assertions.assertTrue(existForSecondAccount);
    }

    // Проверка аргументов
    @Test
    public void testAddForExceptions() {
        Add add = new Add();

        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> add.perform("arg1_name arg2_log arg3_pass arg4"));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> add.perform("arg1_name arg2_log"));
        Exception unknownArgsException3 = assertThrows(UnknownArgsException.class, () -> add.perform("arg1_name arg2_log arg3_pass.withDot"));

        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Вы забыли указать логин или пароль", unknownArgsException2.getMessage());
        Assertions.assertEquals("В пароле не должен содержаться символ '.'", unknownArgsException3.getMessage());
    }


}