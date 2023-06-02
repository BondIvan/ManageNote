package Encrypting;

import java.util.*;

public class TestFormula {

    private static final char[] upper = { 'A', 'B', 'C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };

    private static final char[][] alpha = { {'0', 'a', 'b', 'c', 'd'}, {'e', 'f', 'g', 'h', 'i'}, {'j', 'k', 'l', 'm', 'n'},
            {'o', 'p', 'q', 'r', 's'}, {'t', 'u', 'v', 'w', 'x'}, {'y', 'z', '0', '0', '0'} };

    private static int[] R; // Выполнить analysisString, затем getStarted и потом обнулить массив R (обнуление: установка нулевой ссылки)

    // Формула для нахождения V
    private static String findV(int R) { // Ok

        Calendar calendar = new GregorianCalendar();
        int h = calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.MONTH)+1; // day + month, max = 43, min = 2

        int V =(int) Math.round(Math.PI*Math.pow(h, 2)/3 * ( 3*R - h )); // Формула для нахождения V

        //System.out.println("V = " + V);

        if (V < 0)
            return "0" + Math.abs(V);
        else
            return String.valueOf(V);

    } // Ok

    public static String analysisString(String name) { // Ok

        R = new int[name.length()];

        //System.out.println("Начальная строка - " + name);

        String result = "";

        boolean isUpper = false; // Проверяет заглавная ли сейчас буква
        boolean itsFind = false; // Говорит о том, что искомая буква найдена и дальше искать в матрице не нужно

        // Подумать, стоит ли изменить начальное значение itsNumber с true на false
        boolean itsNumber = true; // Чтобы не производить вычисления для каждой цифры числа (Как-будто разрешение на чтение цифры)
        // Если встретил цифру (itsNumber = true (цифра не рассматривается) ), зашёл в метод analysisNumbers -> получил необходимый result ->
        // -> Поставил itsNumber = false (значит цифра из числа рассматривается) -> ждёшь появления "не цифры", чтобы обновить itsNumber = true

        int count = 0;

        // Текущая буква в слове
        for(int i = 0; i < name.length(); i++) {

            // Получить значения для заглавной буквы, так как метод Character.isLetter() игнорирует заглавные буквы
            if(Character.isUpperCase(name.charAt(i))) {
                isUpper = true;
                // Заменяет все одинаковые буквы
                name = name.replaceFirst( name.substring(i, i+1),  String.valueOf( Character.toLowerCase(name.charAt(i)) )); // Замена заглавной бквы на строчную, для "// Поиск буквы в матрице"
            }

            if(Character.isLowerCase( name.charAt(i) )) {

                if( !itsNumber ) // Если следующим символ не цифра, то
                    itsNumber = true;

                // Поиск буквы в матрице
                for(int k = 0; k < 6; k++) {
                    for(int j = 0; j < 5; j++) {

                        count++; // Количество итераций в 3 циклах for

                        if(alpha[k][j] == name.charAt(i)) {
                            itsFind = true;

                            R[i] = Integer.parseInt(k+""+j);

                            if( isUpper ) { // Добавление символа ^ обозначающий заглавную букву
                                result = result.concat("^").concat(findV( Integer.parseInt(k+""+j) ));
                            } else {
                                result = result.concat(findV( Integer.parseInt(k+""+j) ) );

                            }
                            break; // Прекращает поиск, если буква найдена (прекращает цикл for по j)
                        }

                    }

                    // Прекращает поиск, если буква найдена (после прекращения цикла for по j, проверяет нужно ли прекращать цикл for по k)
                    // (если этого не сделать, продолжит искать по алфивиту после необходимой буквы)
                    if(itsFind) {
                        itsFind = false;
                       break;
                    }
                }

                if(isUpper) // Когда заглавная буква найдена (isUpper = true) => сбросить isUpper
                    isUpper = false;

            } else if(Character.isDigit(name.charAt(i))) { // Если искомый символ цифра
                String analsNum = analysisNumbers(name, i); // .11 12345678912

                if( itsNumber ) {
                    result = result.concat( Objects.requireNonNull( analsNum ) );

                    itsNumber = false;
                }

            } else { // Если искомый символ не буква и не цифра, а специальный символ

                result = result.concat( String.valueOf(name.charAt(i)) );
            }

        }

        //System.out.println("Итоговая строка - " + getStarted(name) + result);

        //System.out.println("До обнуления R: " + Arrays.toString(R));

        String forOutPut = getStarted(name) + result;

        R = null; // Обнуление массива

        //System.out.println("(Число итераций за все циклы)count = " + count);

        return forOutPut; // Для проверки правильности дешифрования с настоящими паролями
    } // Ok

    // Обработка чисел
    private static String analysisNumbers(String name, int currentI) { // Ok

        String result = ""; // Итоговая строка

        int countOfNumbers = 0;
        int countInResult = 0;

        for(int i = currentI; i < name.length(); i++) {
            if(Character.isDigit(name.charAt(i))) {
                countOfNumbers++;

                if(countOfNumbers == 1) { // Это нужно выполнить 1 раз, возможно есть другое условие

                    for(int m = i; m < name.length(); m++) { // Из какого количества цифр состоит число
                        if (Character.isDigit(name.charAt(m)))
                            countInResult++;
                        else
                            break;
                    } //

                    if(countInResult < 10) // Добавление 0, для R[i]. Даёт более конкретно понять сколько цифр, если их > 10
                        result += ".0" + countInResult;
                    else
                        result += "." + countInResult;
                }

                result += name.charAt(i);
            } else { // Если рассматриваемый символ не цифра
                return result; // В result есть необходимое значение, return  выводит его в ситуации, если следующий символ не цифра
            }
        }

        // Случай, если число стоит в конце строки
        return result; // В result есть необходимое значение, return просто выводит его в ситуации, когда следующего символа нет (конец строки)
    }

    private static void analysisOtherSymbol() { // Ok, нет совместимости с analysisString

        String name = "5dK%%DG(;E,*(s#";

        String rez = "";

        Map<Character, Integer> map = new HashMap<>(); // Карту можно заполнить putAll
        map.put('(', 3);
        map.put('#', 4);
        map.put('%', 1);

        for(int i = 0; i < name.length(); i++) {

            if(Character.isLetter(name.charAt(i))) {
                rez += name.charAt(i);
            } else if(Character.isDigit(name.charAt(i))) {
                rez += name.charAt(i);
            } else { // Специальный символ, научиться использовать stream api

                if( map.containsKey(name.charAt(i)) ) {

                    rez += map.get( name.charAt(i) );

                } else {
                    rez += name.charAt(i);
                }

            }

        }

        System.out.println("name - " + name);
        System.out.println("newName - " + rez);

    } // Пока осталось открытым, как заменять спец. символы

    private static String getStarted(String name) { // Ok

        Calendar calendar = new GregorianCalendar();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Отсчёт идёт с 0
        
        String result; // Итоговое значение начальных данных (количество, дата, размеры V)

        if (name.length() < 10)
            result = "0" + name.length();
        else
            result = String.valueOf( name.length() );

        if( day < 10)
            result = result.concat("0" + day);
        else
            result = result.concat( String.valueOf(day) );

        if( month < 10 )
            result = result.concat("0" + month);
        else
            result = result.concat( String.valueOf(month) );

        for (int i : R) {

            if( i != 0)
                result = result.concat(String.valueOf(findV(i).length()));

        }

        return result;
    } // Ok

}
