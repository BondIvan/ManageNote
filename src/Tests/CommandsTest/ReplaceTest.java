package Tests.CommandsTest;

import Commands.WithParameters.Replace;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplaceTest implements TestCommands {

    @Test
    void testReplaceServiceLogin_withoutAccounts() throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com", "vk.account@gmail.com"); note1.setPassword("password_vk");
        notes.add(note1);

        String oldLogin = note1.getLogin();

        Replace replace = new Replace(notes);
        String postfix = "vk.com login newString";
        System.out.println( replace.perform(postfix) );

        Assertions.assertNotEquals(oldLogin, note1.getLogin());
    }

    @Test
    void testReplaceServicePassword_withoutAccounts() throws UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com", "vk.account@gmail.com"); note1.setPassword("password_vk");
        notes.add(note1);

        String oldPassword = note1.getPassword(true);

        Replace replace = new Replace(notes);
        String postfix = "vk.com password newString";
        System.out.println( replace.perform(postfix) );

        Assertions.assertNotEquals(oldPassword, note1.getPassword(true));
    }

    @Test
    @Override
    public void testForExceptions() {

        List<NoteEntity> notes = new ArrayList<>();
        Replace replace = new Replace(notes);

        NoteEntity note1 = new NoteEntity("Vk.com", "first.account@gmail.com");
        NoteEntity note2 = new NoteEntity("Telegram.com", "first.account@gmail.com");
        notes.add(note1);
        notes.add(note2);
        try {
            note1.setPassword("password_vk_1");
            note2.setPassword("password_telegram_2");
        } catch (UnknownArgsException e) {
            System.err.println(e.getMessage());
        }

        Exception accessNotFoundException = assertThrows(AccessNotFoundException.class, () -> replace.perform("arg1_name service arg3_newString"));
        Exception unknownArgsException1 = assertThrows(UnknownArgsException.class, () -> replace.perform("arg1_name arg2_typeAndLess"));
        Exception unknownArgsException2 = assertThrows(UnknownArgsException.class, () -> replace.perform("arg1_name login arg3_newString arg4_andMore"));
        Exception unknownArgsException3 = assertThrows(UnknownArgsException.class, () -> replace.perform("Vk.com arg2_wrongType arg3_newString"));
        Exception incorrectValueException1 = assertThrows(IncorrectValueException.class, () -> replace.perform("Vk.com login first.account@gmail.com"));
        Exception incorrectValueException2 = assertThrows(IncorrectValueException.class, () -> replace.perform("Telegram.com service Vk.com"));

        Assertions.assertEquals("Сервис не найден", accessNotFoundException.getMessage());
        Assertions.assertEquals("Параметров меньше чем нужно", unknownArgsException1.getMessage());
        Assertions.assertEquals("Параметров больше чем нужно", unknownArgsException2.getMessage());
        Assertions.assertEquals("Неизвестный параметр", unknownArgsException3.getMessage());
        Assertions.assertEquals("У этого сервиса такой логин уже существует", incorrectValueException1.getMessage()); //Если менять только логин
        Assertions.assertEquals("Такой логин уже есть", incorrectValueException2.getMessage()); //Если менять только логин
    }
}