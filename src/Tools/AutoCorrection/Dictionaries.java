package Tools.AutoCorrection;

import Entity.NoteEntity;
import Tools.UsefulMethods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionaries {

    // Список уникальных названий сервисов, без аккаунтов (для автокоррекции)
    public static Set<String> uniqueServiceNames = new HashSet<>(); // Задаётся значение по умолчанию, чтобы не ловить ошибку NullPointerException,
    // если использовать команду без предварительного заполнения словаря (uniqueServiceNames)

    public void fillingDictionaries(List<NoteEntity> listWithNotes) {

        uniqueServiceNames = new HashSet<>(UsefulMethods.getAllUniqueServiceName(listWithNotes));

    }

}
