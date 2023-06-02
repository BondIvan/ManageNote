import Entity.NoteEntity;
import Tools.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final static String testFile = "ForTesting.txt";
    private static final String workFile = "Access.txt";
    private static final String path = "C:\\My place\\Java projects\\MyNewTest_firstTry\\src\\ForTxtFiles\\" + testFile;

    // Список всех доступов
    public static List<NoteEntity> notes = new ArrayList<>();

    public static void main(String[] args) {

    }

    public static void init() throws IOException {

        Tools.getAllNoteFromFile(path);


    }


}
