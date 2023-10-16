package Tools.AutoCorrection;

import Entity.NoteEntity;
import Tools.UsefulMethods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionaries {

    // Список уникальных названий сервисов, без аккаунтов (для автокоррекции)
    public static Set<String> uniqueServiceNames;

    public void fillingDictionaries(List<NoteEntity> listWithNotes) {

        uniqueServiceNames = new HashSet<>(UsefulMethods.getAllUniqueServiceName(listWithNotes));

    }

}
