package Tests;

import Commands.WithParameters.Add;
import Entity.NoteEntity;
import Source.StartConsole;
import Tools.UsefulMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AddTest extends Add {

    @Test
    void testPerform() throws Exception {

        List<NoteEntity> listWithNotes = UsefulMethods.getAllNoteFromFile(StartConsole.PATH);
        int actual = 72;

        Add add = new Add();
        System.out.println( add.perform("Test Log_test pass_test") );

        int expected = listWithNotes.size();

        Assertions.assertEquals(expected, actual); // Не то что нужно
    }

}