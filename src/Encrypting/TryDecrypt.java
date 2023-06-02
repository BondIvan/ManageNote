package Encrypting;

public class TryDecrypt {

    private static final char[] upper = { 'A', 'B', 'C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };

    private static final char[][] alpha = { {'0', 'a', 'b', 'c', 'd'}, {'e', 'f', 'g', 'h', 'i'}, {'j', 'k', 'l', 'm', 'n'},
            {'o', 'p', 'q', 'r', 's'}, {'t', 'u', 'v', 'w', 'x'}, {'y', 'z', '0', '0', '0'} };

    // TODO Problem
    //  resulting нигде не обнуляется, и если несколько подрят сделать дешифровок, пароли будут идти один за одним в одной строке Exp:(pass1pass2pass3...)
    private static String resulting = ""; // Результат дешифровки

    private static int getCountOfLetter(String name) { // Определение количества букв, с учётом вычитания цифр и спец. символов

        int count = Integer.parseInt( name.substring(0, 2) ); // Неточный (нужно именно количество букв, без цифр и спец. символов) count из зашифрованной строки

       // System.out.println("Before: " + count);

        int sizeNum;
        for (int i = 0; i < name.length(); i++) {

            if( !Character.isDigit( name.charAt(i) ) ) { // Если НЕ число
                if (name.charAt(i) == '.') {
                    i++; // Прохождение символа точки ('.') путём добавления i + 1

                    sizeNum = Integer.parseInt(name.substring(i, i + 2)); // Определяет количество цифр в числе .3525 -> 3
                    count -= sizeNum; // Вычитание из неточного count sizeNum
                }
                else if (name.charAt(i) != '^') { // Все специальные символы также учтены в count, поэтому нужно их отнять
                    count--;
                }
            }

        }
        // System.out.println("After: " + count);

        return count;
    } // Ok

    private static void itsNumber(int number) { // Ok
        resulting += number;
    } // Ok

    private static void itsLetter(int letter_V, boolean isUpper, int h) { // Ok

        int R = (int) Math.round(letter_V/Math.pow(h, 2)/Math.PI + (double)h/3);

        int y = R % 10;
        int x = (R - y) / 10;

        if(isUpper)
            resulting += Character.toUpperCase( alpha[x][y] );
        else
            resulting += alpha[x][y];

    } // Ok

    private static void itsSpecSymbol(char symbol) { // Ok
        resulting += symbol;
    } // Ok

    public static String decrypt(String name) { // Ok

        String resulting;

        int countLetter = getCountOfLetter(name); // Сколько именно БУКВ зашифрованно, без цифр/чисел и спец. символов
        int day = Integer.parseInt( name.substring(2,4) );
        int month = Integer.parseInt( name.substring(4, 6) );
        int h = day + month;
        String countNumberInV = name.substring(6, 6+countLetter);
        int[] sizeV = new int[countLetter]; // Сколько цифр в числе V для каждой буквы

        for(int i = 0; i < countLetter; i++) {
            sizeV[i] = Integer.parseInt( countNumberInV.substring(i,i+1) );
        }

        String stroka = name.substring(6+countLetter); // Основная часть зашифрованной строки

        int[] V = new int[countLetter]; // Значения V для каждой буквы
        int howMuchNum; // Сколько  цифр в зашифрованном числе
        boolean isUpper = false; // Сигнал, является ли буква заглавной

        // Разбитие порядка цифр на отдельные значения, учитывая регистр, цифры и спец. символы
        for(int i = 0, v = 0; i < stroka.length();) {

                if (stroka.charAt(i) == '^') {
                    i++; // +1 перескочить через '^'
                    isUpper = true;
                } else if (stroka.charAt(i) == '.') {
                    i++; // +1 перескочить через '.'

                    howMuchNum = Integer.parseInt(stroka.substring(i, i+2)); // +2 учитывая 0 в 03
                    itsNumber( Integer.parseInt( stroka.substring(i+2, i+2 + howMuchNum) ) ); // Дописывание в результат цифр

                    i = i+2 + howMuchNum; // +2 учитывая 0 в 03, перепрыгивающие шаги для i на количество цифр в числе

                    continue;
                } else if(!Character.isDigit(stroka.charAt(i))) { // Для специальных символов
                    itsSpecSymbol( stroka.charAt(i) ); // Дописывание в результат спец. символа
                    i++;

                    continue;
                }

                String oneV = stroka.substring(i, i + sizeV[v]); // Значение V для одной буквы

            // System.out.println("oneV: " + oneV);

            if(oneV.charAt(0) == '0') // Проверка на отрицательное V
                itsLetter(- Integer.parseInt( oneV ), isUpper, h);
            else
                itsLetter(Integer.parseInt( oneV), isUpper, h);

            if(isUpper) // Когда заглавная буква найдена (isUpper = true) => сбросить isUpper
                isUpper = false;

            i = i + sizeV[v]; // Перепрыгивающие шаги для i, на количество цифр в V для одной буквы
            v++; // Переход к следующему значению sizeV, должен происходить только в том случае, если рассматривается именно буква
            // Подумать над более лучшей реализацией if-ов выше (из-за двух continue)
        }

        // TODO Временное решение
        resulting = TryDecrypt.resulting;
        TryDecrypt.resulting = "";

        return resulting;
    } // Ok

}
