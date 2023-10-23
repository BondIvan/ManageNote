package Tests.CommandsTest;

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

    //TODO Добавить тест на получение сервиса по логину

    @Test
    void testGetByLogin() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Vk.com (3-rd account)", "third.account@gmail.com"); note3.setPassword("password_vk_3");
        NoteEntity note4 = new NoteEntity("Vk.com (4-th account)", "fourth.account@gmail.com"); note4.setPassword("password_vk_4");
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);
        notes.add(note4);

        String login1 = "first.account@gmail.com";
        String login3 = "third.account@gmail.com";
        String login_no = "no.account@gmail.com";

        NoteEntity noteEntity1 = notes.stream()
                .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase("vk.com")) // Сравнивается первое слово текущего сервиса с требуемым
                .filter(note -> note.getLogin().equalsIgnoreCase(login1))
                .findFirst()
                .orElseThrow(() -> new AccessNotFoundException("Сервис не найден"));

        NoteEntity noteEntity3 = notes.stream()
                .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase("vk.com"))
                .filter(note -> note.getLogin().equalsIgnoreCase(login3))
                .findFirst()
                .orElseThrow(() -> new AccessNotFoundException("Сервис не найден"));

        Exception noteEntity_noByLogin = assertThrows(AccessNotFoundException.class, () ->
                        notes.stream()
                        .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase("vk.com"))
                        .filter(note -> note.getLogin().equalsIgnoreCase(login_no))
                        .findFirst()
                        .orElseThrow(() -> new AccessNotFoundException("Сервис не найден")));

        Exception noteEntity_noByServiceName = assertThrows(AccessNotFoundException.class, () ->
                notes.stream()
                        .filter(note -> note.getIdService().split(" ")[0].equalsIgnoreCase("kvvvv.com"))
                        .filter(note -> note.getLogin().equalsIgnoreCase(login3))
                        .findFirst()
                        .orElseThrow(() -> new AccessNotFoundException("Сервис не найден")));

        Assertions.assertEquals(note1, noteEntity1);
        Assertions.assertEquals(note3, noteEntity3);

        Assertions.assertEquals("Сервис не найден", noteEntity_noByLogin.getMessage());
        Assertions.assertEquals("Сервис не найден", noteEntity_noByServiceName.getMessage());
    }

    @Test
    void testIgnoreCase() throws IOException, AccessNotFoundException, UnknownArgsException {

        List<NoteEntity> notes = UsefulMethods.getAllNoteFromFile("C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\ForTesting.txt");

        Dictionaries dictionaries = new Dictionaries();
        dictionaries.fillingDictionaries(notes);

        Get get = new Get(notes);

        String postfix1 = "Telegram.com";
        String postfix2 = "teleGram.com";

        String expected1 = """
                Telegram.com
                Login: Anien
                Password: guardianWith525Shield""";

        String expected2 = """
                Telegram.com
                Login: Anien
                Password: guardianWith525Shield""";

        // Проверка возвращаемого результа
        Assertions.assertEquals(expected1, get.perform(postfix1));
        Assertions.assertEquals(expected2, get.perform(postfix2));

    }

    // Проверка аргументов
    @Override
    @Test
    public void testForExceptions() {

        List<NoteEntity> notes;
        try {
            notes = UsefulMethods.getAllNoteFromFile("C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\ForTesting.txt");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return;
        }

        Get get = new Get(notes);

        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class, () -> get.perform("Telegram"));
        Exception unknownArgsException1 = assertThrows(OptionsExceptions.UnknownArgsException.class, () -> get.perform("Telegram.com arg2"));
        Exception unknownArgsException2 = assertThrows(OptionsExceptions.UnknownArgsException.class, () -> get.perform(""));

        Assertions.assertEquals("Сервис не найден", accessNotFoundException.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Нет параметров", unknownArgsException2.getMessage());
    }

}