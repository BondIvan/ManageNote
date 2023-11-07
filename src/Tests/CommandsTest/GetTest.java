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

    @Test
    void testGetByLogin() throws UnknownArgsException, AccessNotFoundException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Vk.com (3-rd account)", "third.account@gmail.com"); note3.setPassword("password_vk_3");
        NoteEntity note4 = new NoteEntity("Vk.com (4-th account)", "fourth.account@gmail.com"); note4.setPassword("password_vk_4");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4);

        String[] args = "vk.com".split(" ");
        NoteEntity workNote1 = UsefulMethods.getAccountFromServiceByLogin(notes, args[0], "third.account@gmail.com");
        NoteEntity workNote2 = UsefulMethods.getAccountFromServiceByLogin(notes, args[0], "first.account@gmail.com");

        Assertions.assertEquals(note3, workNote1);
        Assertions.assertEquals(note1, workNote2);
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