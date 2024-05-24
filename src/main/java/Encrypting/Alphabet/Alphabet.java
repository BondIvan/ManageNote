package Encrypting.Alphabet;

public class Alphabet {

    //TODO Изменение порядка в alpha пока не реализовано

//    private static char[][] alpha;
//
//    public static void setAlpha() {
//    }

    public static char[][] getAlpha() {

        //TODO Узнать для чего в подмассивах стоят нули, а конкретно первый 0 ->
        // 0 был нужен для того, чтобы если первый символ 'a', то были какие-то проблемы с V (отрицательные значения или что-то в этом роде)
        // -> нужно перепроверить это
        // Без 1-го 0 нормально работает )

        return new char[][] {

                {'a', 'b', 'c', 'd', 'e'}, {'f', 'g', 'h', 'i', 'j'}, {'k', 'l', 'm', 'n', 'o'},
                {'p', 'q', 'r', 's', 't'}, {'u', 'v', 'w', 'x', 'y'}, {'z', '0', '0', '0', '0'},
                {'A', 'B', 'C', 'D', 'E'}, {'F', 'G', 'H', 'I', 'J'}, {'K', 'L', 'M', 'N', 'O'},
                {'P', 'Q', 'R', 'S', 'T'}, {'U', 'V', 'W', 'X', 'Y'}, {'Z', '0', '0', '0', '0'}

        };
    }

    // Изменение порядка символов в alpha
    public static char[][] updateAlpha() {

        return null;
    }

}
