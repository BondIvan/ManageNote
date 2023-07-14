package Encrypting;

import java.time.LocalDate;
import java.util.List;

import Tools.Converting.ConvertCharArrayToLists;

public class ViewEncrypt {
    private final List<List<Character>> characters; // Список, который содержит списки, которые содержат символы строчных/заглавных букв по 5 штук
    private final int day; // Текущий день
    private final int month; // Текущий месяц

    public ViewEncrypt(char[][] alpha) {
        LocalDate localDate = LocalDate.now();
        day = localDate.getDayOfMonth(); // Текущий день
        month = localDate.getMonthValue(); // Текущий месяц

        characters = ConvertCharArrayToLists.convert(alpha);
    }

    // (Первая часть зашифр. строки) Создаёт строку, которая будет содержать информацию для расшифровки (количество символов в строке, дату, количество цифр в V)
    private StringBuilder encryptedStringStartWith(String text) {

        StringBuilder start = new StringBuilder();
        // Добавление 0, для цифр меньше 10, чтобы проще было определить двузначные числа
        if (text.length() < 10)
            start.append("0").append( text.length() );
        else
            start.append( text.length() );

        if( day < 10)
            start.append("0").append( day );
        else
            start.append( day );

        if( month < 10 )
            start.append("0").append( month ) ;
        else
            start.append( month );

        return start;
    }

    public String encrypting(String text) {

        StringBuilder resultOfEncrypting = new StringBuilder(); // Результат шифрования строки
        StringBuilder startOfEncryptedString = encryptedStringStartWith(text); // Строка, содержашая информацию для расшифровки (количество символов в строке, дату, количество цифр в V)

        String digitsInNumbers = ""; // Содержит все цифры встреченного числа, чтобы добавить в resultOfEncrypting (.0х + digitsInNumbers)
        for(char sym: text.toCharArray()) {

            if( !Character.isLetterOrDigit(sym) )
                resultOfEncrypting.append(sym);

            if( Character.isDigit(sym) ) { // Если символ - цифра
                digitsInNumbers += sym;

                continue; // Если sym - число, то нет смысла проверять дальше
                // (просто записываем его в digitsInNumbers и переходим к след. символу, если оно тоже число, повторяем действия,
                // если уже не число, то у нас есть и количество цифр в числе и какие это цифры)
            }

            if(digitsInNumbers.length() != 0) { // После подсчёта цифр в числе, записать выражение .0х + digitsInNumbers в resultOfEncrypting
                if (digitsInNumbers.length() < 10)
                    resultOfEncrypting.append(".0").append(digitsInNumbers.length()).append(digitsInNumbers);
                else
                    resultOfEncrypting.append(".").append(digitsInNumbers.length()).append(digitsInNumbers);
                digitsInNumbers = "";
            }

            for (int i = 0; i < characters.size(); i++) { // Перебор по внутренним спискам characters
                if (characters.get(i).contains(sym)) {

                    String V = findV(Integer.parseInt(i+""+characters.get(i).indexOf(sym))); // Вычисление V для символа
                    resultOfEncrypting.append( V );
                    startOfEncryptedString.append( V.length() );

                    break;
                }
            }

        }

        if(digitsInNumbers.length() != 0) { // Записать цифры в resultOfEncrypting, если это последние символы строки text
            if (digitsInNumbers.length() < 10)
                resultOfEncrypting.append(".0").append(digitsInNumbers.length()).append(digitsInNumbers);
            else
                resultOfEncrypting.append(".").append(digitsInNumbers.length()).append(digitsInNumbers);
        }

        // Соединение строки, содержащей информацию для расшифровки и зашифрованной строки
        return startOfEncryptedString.append( resultOfEncrypting ).toString();
    }

    // Формула шифрования
    private String findV(int R) { // Ok

        int h = day + month; // day + month, max = 43, min = 2
        int V = (int)Math.round(Math.PI*Math.pow(h, 2)/3 * ( 3*R - h )); // Формула для нахождения V

        //System.out.println("V = " + V);

        if (V < 0)
            return "0" + Math.abs(V);
        else
            return String.valueOf(V);

    }

}


