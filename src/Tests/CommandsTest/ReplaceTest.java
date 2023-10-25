package Tests.CommandsTest;

import Commands.WithParameters.Replace;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplaceTest implements TestCommands {

    @Test
    void test() {

    }

    @Test
    @Override
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        Replace replace = new Replace(notes);

        NoteEntity note1 = new NoteEntity("Vk.com", "first.account@gmail.com");
        notes.add(note1);
        try {
            note1.setPassword("password_vk_1");
        } catch (UnknownArgsException e) {
            System.err.println(e.getMessage());
        }


        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class, () -> replace.perform("arg1_name arg2_type arg3_newString"));
        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> replace.perform("arg1_name arg2_typeAndLess"));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> replace.perform("arg1_name arg2_type arg3_newString arg4_andMore"));
        Exception unknownArgsException3 = assertThrows(UnknownArgsException.class, () -> replace.perform("Vk.com arg2_wrongType arg3_newString"));

        Assertions.assertEquals("Сервис не найден", accessNotFoundException.getMessage());
        Assertions.assertEquals("Параметров меньше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException2.getMessage());
        Assertions.assertEquals("Неизвестный параметр в изменении", unknownArgsException3.getMessage());
    }
}