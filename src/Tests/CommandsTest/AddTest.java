package Tests.CommandsTest;

import Commands.WithParameters.Add;
import Entity.NoteEntity;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AddTest implements TestCommands {

    /***
     * Закоментировать подтверждение в Add.class перед выполнением
     * <p>
     * Проверка всех ситуаций в методе perform в классе Add.class
     ***/

    // Проверка создания каждого номера аккаунта
    @Test
    void testNextAccountForAlreadyExistService() throws UnknownArgsException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        notes.add(note1);
        notes.add(note2);

        Add add = new Add(notes);
        for(int i = 3; i < 11; i++) {
            System.out.println( add.perform("Vk.com " + i +".account@gmail.com password_vk_" + i) );
        }

        String[] numberOfAccount = {"1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th"}; // 10 "аккаунтов" максимум
        for(int i = 0; i < notes.size(); i++) {
            Assertions.assertEquals(notes.get(i).getIdService(), "Vk.com (" + numberOfAccount[i] + " account)");
        }

    }

    // Добавление нового сервиса
    @Test
    void testNewService() throws UnknownArgsException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note = new NoteEntity("Vk.com", "first.account@gmail.com"); note.setPassword("password_vk_1");
        notes.add(note);

        Add add = new Add(notes);
        System.out.println( add.perform("Test.com test_log test_123_pass") );

        boolean existNewService = notes.stream()
                        .anyMatch(noteNew -> noteNew.getIdService().equalsIgnoreCase("Test.com"));

        Assertions.assertTrue(existNewService);
    }

    // Проверка добавления второго аккаунта к уже существующему сервису, также проверка на работоспособность с LowerCase
    @Test
    void testForNewAccountForAlreadyExistService() throws UnknownArgsException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note = new NoteEntity("Vk.com", "first.account@gmail.com"); note.setPassword("password_vk_1");
        notes.add(note);

        String postfix = "vk.com 375257291200 password_vk_2";

        Add add = new Add(notes);
        System.out.println( add.perform(postfix) ); // Добавление нового аккаунта к сервису Vk.com

        boolean existForSecondAccount = notes.stream()
                .anyMatch(note1 -> note1.getIdService().equalsIgnoreCase("Vk.com (2-nd account)"));

        Assertions.assertEquals("Vk.com (1-st account)".toLowerCase(), notes.get(0).getIdService().toLowerCase());
        Assertions.assertTrue(existForSecondAccount, "Сервис не был добавлен");
    }

    // Проверка аргументов
    @Override
    @Test
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        Add add = new Add(notes);

        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> add.perform("arg1_name arg2_log arg3_pass arg4"));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> add.perform("arg1_name arg2_log"));
        Exception unknownArgsException3 = assertThrows(UnknownArgsException.class, () -> add.perform("arg1_name arg2_log arg3_pass.withDot"));

        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Вы забыли указать логин или пароль", unknownArgsException2.getMessage());
        Assertions.assertEquals("В пароле не должен содержаться символ '.'", unknownArgsException3.getMessage());
    }

    @Test
    void testIncorrectValueException() throws UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note = new NoteEntity("Vk.com", "first.account@gmail.com"); note.setPassword("password_vk_1");
        notes.add(note);

        String postfix = "vk.com first.account@gmail.com password_vk_2";

        Add add = new Add(notes);
        Exception incorrectValueException = assertThrows(IncorrectValueException.class, () -> add.perform(postfix));

        Assertions.assertEquals("У этого сервиса такой логин уже существует", incorrectValueException.getMessage());
    }

}