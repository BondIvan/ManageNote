package Encrypting.MyEncrypt.Alphabet;

public class Alphabet {

    // Изменение порядка в alpha пока не реализовано

//    private static char[][] alpha;
//
//    public static void setAlpha() {
//    }

    public static char[][] getAlpha() {

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
