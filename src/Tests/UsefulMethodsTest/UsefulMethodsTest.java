package Tests.UsefulMethodsTest;

import Entity.NoteEntity;
import OptionsExceptions.UnknownArgsException;
import Tools.UsefulMethods;
import org.glassfish.grizzly.http.Note;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class UsefulMethodsTest extends UsefulMethods {

    @Test
    void testChangingNameWhenAdd() throws UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "vk_first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "vk_second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg_first.account@gmail.com"); note2.setPassword("password_teleg_1");
        notes.add(note1); notes.add(note2); notes.add(note3);

        String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 аккаунтов максимум

        // Добавить аккаунт к сервису без аккаунтов
        NoteEntity newNote1 = new NoteEntity("Telegram.com", "teleg_second.account@gmral.com"); newNote1.setPassword("password_teleg_2");
        notes.add(newNote1);
        UsefulMethods.changingNameWhenAdd(notes, "Telegram.com");

        Assertions.assertEquals("Telegram.com (1-st account)", note3.getIdService());
        Assertions.assertEquals("Telegram.com (2-nd account)", newNote1.getIdService());

        // Добавление нового аккаунта у сервиса, до 10 аккаунтов
        for(int i = 3; i < 11; i++) {
            NoteEntity newNote = new NoteEntity("Vk.com", "vk_" + i + ".account@gmail.com"); newNote.setPassword("password_vk_" + i);
            notes.add(newNote);
            UsefulMethods.changingNameWhenAdd(notes, "Vk.com");
        }

        // Добавление нового аккаунта у сервиса, до 10 аккаунтов
        for(int i = 0, k = 0; i < notes.size(); i++) {
            if(!notes.get(i).getIdService().contains("Vk.com"))
                continue;

            Assertions.assertEquals("Vk.com (" + numberOfAccount[k] + " account)", notes.get(i).getIdService());
            k++;
        }

    }

    @Test
    void testChangingNameWhenRemove() throws UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "vk_first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "vk_second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Vk.com (3-rd account)", "vk_third.account@gmail.com"); note3.setPassword("password_vk_3");
        NoteEntity note4 = new NoteEntity("Telegram.com (1-st account)", "first_teleg.account@gmail.com"); note4.setPassword("password_teleg_1");
        NoteEntity note5 = new NoteEntity("Telegram.com (2-nd account)", "second_teleg.account@gmail.com"); note5.setPassword("password_teleg_2");
        NoteEntity note6 = new NoteEntity("Vk.com (4-th account)", "vk_fourth.account@gmail.com"); note6.setPassword("password_vk_4");
        NoteEntity note7 = new NoteEntity("Vk.com (5-th account)", "vk_fifth.account@gmail.com"); note7.setPassword("password_vk_5");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5); notes.add(note6); notes.add(note7);

        String[] numberOfAccount = { "1-st", "2-nd", "3-rd", "4-th", "5-th", "6-th", "7-th", "8-th", "9-th", "10-th" }; // 10 аккаунтов максимум

        // Удаление одного из двух аккаунтов у сервиса
        notes.remove(note5);
        UsefulMethods.changingNameWhenRemove(notes, "Telegram.com");

        Assertions.assertEquals("Telegram.com", note4.getIdService());

        // Удаление одного из многих аккаунтов сервиса (из центра)
        notes.remove(note3);
        UsefulMethods.changingNameWhenRemove(notes, "Vk.com");
        for(int i = 0, k = 0; i < notes.size(); i++) {
            if(!notes.get(i).getIdService().contains("Vk.com"))
                continue;

            Assertions.assertEquals("Vk.com (" + numberOfAccount[k] + " account)", notes.get(i).getIdService());
            k++;
        }

        // Удаление одного из многих аккаунтов сервиса (первого)
        notes.remove(note1);
        UsefulMethods.changingNameWhenRemove(notes, "Vk.com");
        for(int i = 0, k = 0; i < notes.size(); i++) {
            if(!notes.get(i).getIdService().contains("Vk.com"))
                continue;

            Assertions.assertEquals("Vk.com (" + numberOfAccount[k] + " account)", notes.get(i).getIdService());
            k++;
        }

        // Удаление одного из многих аккаунтов сервиса (последнего)
        notes.remove(note7);
        UsefulMethods.changingNameWhenRemove(notes, "Vk.com");
        for(int i = 0, k = 0; i < notes.size(); i++) {
            if(!notes.get(i).getIdService().contains("Vk.com"))
                continue;

            Assertions.assertEquals("Vk.com (" + numberOfAccount[k] + " account)", notes.get(i).getIdService());
            k++;
        }

    }

    @Test
    void testGetAllAccountsForOneService() throws UnknownArgsException {

        List<NoteEntity> notes = new ArrayList<>();
        NoteEntity note1 = new NoteEntity("Vk.com (1-st account)", "vk_first.account@gmail.com"); note1.setPassword("password_vk_1");
        NoteEntity note2 = new NoteEntity("Vk.com (2-nd account)", "vk_second.account@gmail.com"); note2.setPassword("password_vk_2");
        NoteEntity note3 = new NoteEntity("Telegram.com", "teleg.account@gmail.com"); note3.setPassword("password_teleg_1");
        NoteEntity note4 = new NoteEntity("logo.com", "logo.account@gmail.com"); note4.setPassword("password_logo_1");
        NoteEntity note5 = new NoteEntity("Yandex.ru", "yandex.account@gmail.com"); note5.setPassword("password_yandex_1");
        NoteEntity note6 = new NoteEntity("Vk.com (3-rd account)", "vk_third.account@gmail.com"); note6.setPassword("password_vk_3");
        notes.add(note1); notes.add(note2); notes.add(note3); notes.add(note4); notes.add(note5); notes.add(note6);

        // Аккаунты из общего списка (notes) используя метод
        Object[] accountsFromOneService = UsefulMethods.getAllAccountsForOneService(notes, "Vk.com").toArray();

        // Аккаунты из общего списка (notes) добавлены вручную, для проверки
        Object[] accFromNotes = { note1, note2, note6 };

        Assertions.assertArrayEquals(accFromNotes, accountsFromOneService);
    }

    @Test
    void testGetAccountFromServiceByLogin() {
    }

    @Test
    void testMakeArgsTrue() {

        String postfixGet = "   Vk.com  ";
        String postfixAdd = "  Vk.com  first_vk.account@gmail.com     password_vk_1   ";
        String postfixDelete = "  Vk.com  second_vk.account@gmail.com  ";
        String postfixReplace = "  Vk.com  third_vk.account@gmail.com   newString  ";

        String[] argsGet = UsefulMethods.makeArgsTrue(postfixGet);
        String[] argsAdd = UsefulMethods.makeArgsTrue(postfixAdd);
        String[] argsDelete = UsefulMethods.makeArgsTrue(postfixDelete);
        String[] argsReplace = UsefulMethods.makeArgsTrue(postfixReplace);

        Assertions.assertArrayEquals(new String[] {"Vk.com"}, argsGet);
        Assertions.assertArrayEquals(new String[] {"Vk.com", "first_vk.account@gmail.com", "password_vk_1"}, argsAdd);
        Assertions.assertArrayEquals(new String[] {"Vk.com", "second_vk.account@gmail.com"}, argsDelete);
        Assertions.assertArrayEquals(new String[] {"Vk.com", "third_vk.account@gmail.com", "newString"}, argsReplace);
    }

    @Test
    void testSortNoteEntityByServiceName() {
    }

    @Test
    void testGetAllUniqueServiceName() {
    }
}