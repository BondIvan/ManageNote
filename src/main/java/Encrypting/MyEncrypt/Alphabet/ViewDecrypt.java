package Encrypting.MyEncrypt.Alphabet;

import java.util.ArrayList;
import java.util.List;

import Tools.Converting.ConvertCharArrayToLists;

public class ViewDecrypt {

    private final List<List<Character>> characters;

    public ViewDecrypt(char[][] alpha) {
        characters = ConvertCharArrayToLists.convert(alpha);
    }

    // Дешифрование
    public String decrypt(String encText) {

        int countLetter = getCountOfLetter(encText); // Сколько именно БУКВ зашифрованно, без цифр/чисел и спец. символов
        int day = Integer.parseInt(encText.substring(2, 4)); // День
        int month = Integer.parseInt(encText.substring(4, 6)); // Месяц
        int h = day + month; // Параметр для формулы V
        int startPosition = 6 + countLetter; // Индекс в зашифрованной строке, где начинается искомый зашифрованный текст

        int[] sizeV = getLengthEvery_V_fromFirstPartEncryptString(encText.substring(6, startPosition)); // Количество цифр в каждом числе V

        List<String> V_values = getSpecificDigitalValuesForEvery_V(sizeV, encText, startPosition); // Значения V (с обозначениями цифры или если V отрицательное)

        StringBuilder decryptString = new StringBuilder(); // Результат расшифровки
        for (String value : V_values)
            decryptString.append(getLetterFrom_V(value, h));

        //TODO Проверить на несколько расщифровок подрят, чтобы не было накопления расшифровок в одной строке, как в прошлой версии
        return decryptString.toString();
    }

    // Определение количества букв, с учётом вычитания цифр и спец. символов
    private int getCountOfLetter(String encText) {

        int countLetter = Integer.parseInt(encText.substring(0, 2)); // Неточный countLetter из зашифрованной строки (нужно именно количество букв, без цифр и спец. символов)
        if (encText.contains(".")) { // Если нет символа '.', нет смысла искать '.' и вычислять количество цифр в числе

            for (int i = 0; i < encText.length(); i++) {
                if (encText.charAt(i) == '.') {
                    i++; // Переступить через символ точки ('.')

                    int sizeNum = Integer.parseInt(encText.substring(i, i + 2)); // Для каждого числа в строке определяет количество цифр в числе (.3525 -> 3)
                    countLetter -= sizeNum; // Вычитание из неточного count sizeNum
                }

            }

// Ещё один способ, здесь сразу находится позиция символа '.' через метод indexOf (он работает медленее)
//            int positionFindDot = encText.indexOf("."); // Позиция первого сивого символа '.'
//
//            while(positionFindDot >= 0) { // positionFindDot < 0, когда других точек больше не найдено
//
//                int sizeNum = Integer.parseInt(encText.substring(positionFindDot+1, positionFindDot + 3)); // Определяет количество цифр в числе .3525 -> 3
//                countLetter -= sizeNum;
//
//                positionFindDot = encText.indexOf(".", positionFindDot + 1); // Делает +1 шаг от позиции текущей точки
//            }
        }

        // Обработка специальных символов (Вычитание из общего числа символов, специального символа из зашифрованной строки)
        for (int i = 0; i < encText.length(); i++) {
            char c = encText.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '.') { // Символ '.' не должен вычитаться, так как он нужен для дальнейших операций
                countLetter--;
            }
        }

        return countLetter;
    }

    // Определение количество цифр в каждом числе V из первой части зашифрованной строки
    private int[] getLengthEvery_V_fromFirstPartEncryptString(String allLengthsOfV) {

        int[] lengths_V = new int[allLengthsOfV.length()];

        for (int i = 0; i < lengths_V.length; i++)
            lengths_V[i] = Character.digit(allLengthsOfV.charAt(i), 10);

        return lengths_V;
    }

    // Получение значения (числа) каждого V в отдельности в виде списка из зашифрованной строки
    private List<String> getSpecificDigitalValuesForEvery_V(int[] sizeV, String encText, int startPosition) {
        List<String> values = new ArrayList<>(); // Список значений каждого V

        String text = encText.substring(startPosition); // Искомый зашифрованный текст
        for (int i = 0, forSizeV = 0; i < text.length(); ) {

            if (text.charAt(i) == '.') { // Если стоит . -> это число
                i = i + 1; // Переступить через '.'
                int sizeDigit = Integer.parseInt(text.substring(i, i + 2));
                i = i + 2; // переступить через 03 (+2 учитывая 0 в 03)
                values.add("." + text.substring(i, i + sizeDigit));

                i = i + sizeDigit; // Переступить через 03 и количество цифр в числе

                continue;
            }

            if (!Character.isDigit(text.charAt(i))) { // Если найден специальный символ, просто пропустить его
                values.add(String.valueOf(text.charAt(i)));
                i++;

                continue;
            }

            values.add(text.substring(i, i + sizeV[forSizeV]));
            i = i + sizeV[forSizeV]; // Шаг на ширину размера текущего V, в строке text
            forSizeV++;
        }

        return values;
    }

    // Получение буквы из значения (числа) V
    private String getLetterFrom_V(String V_value, int h) {

        if (V_value.contains(".")) { // Число
            V_value = V_value.substring(1); // Символ '.' обрезаем и возвращаем число

            return V_value;
        }

        if (!Character.isLetterOrDigit(V_value.charAt(0))) { // Специальный символ
            return V_value;
        }

        int V = Integer.parseInt(V_value);

        if (V_value.charAt(0) == '0') { // Отрицательное V
            V = -V;
        }

        int R = (int) Math.round(V / Math.pow(h, 2) / Math.PI + (double) h / 3); // Формула расшифрования
        int position = R % 10;
        int lvl = (R - position) / 10;

        return String.valueOf(characters.get(lvl).get(position));
    }

}
