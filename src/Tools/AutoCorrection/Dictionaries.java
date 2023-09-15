package Tools.AutoCorrection;

import Entity.NoteEntity;
import Tools.UsefulMethods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionaries {

    // Список уникальных названий сервисов, без аккаунтов (для автокоррекции)
    public static Set<String> uniqueServiceNames;

    // Список всех записей (сервисов)
    private final List<NoteEntity> listWithNotes;
    public Dictionaries(List<NoteEntity> list) {
        this.listWithNotes = list;
    }

    public void fillingDictionaries() {

        uniqueServiceNames = new HashSet<>(UsefulMethods.getAllUniqueServiceName(listWithNotes));

    }

}
