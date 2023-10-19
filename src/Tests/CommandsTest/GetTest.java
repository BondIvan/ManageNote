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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GetTest {

    @Test
    void testPerform() throws IOException, AccessNotFoundException, UnknownArgsException {

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


        // Проверка аргументов
        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class, () -> get.perform("Telegram"));
        Exception unknownArgsException1 = assertThrows(OptionsExceptions.UnknownArgsException.class, () -> get.perform("Telegram.com arg2"));
        Exception unknownArgsException2 = assertThrows(OptionsExceptions.UnknownArgsException.class, () -> get.perform(""));

        Assertions.assertEquals("Сервис не найден", accessNotFoundException.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Нет параметров", unknownArgsException2.getMessage());

    }

}