package Tools.AutoCorrection;

import java.util.Arrays;
import java.util.List;

public class AutoCorrectionServiceName {

    // Словарь правильных названий
    private static final List<String> dictionary = Arrays.asList( "Google.com (1-st account)", "Google.com (2-nd account)");

    public static String suggestCorrection(String inputString) {

        inputString = inputString.toLowerCase();

        int minDistance = Integer.MAX_VALUE;
        String closestWord = inputString;

        for(String nameFromDictionary: dictionary) {
            int distanceForInputString = levenshteinDistance(inputString, nameFromDictionary);

            if( distanceForInputString < minDistance ) {
                minDistance = distanceForInputString;
                closestWord = nameFromDictionary;
            }
        }

        return closestWord;
    }

    // Вычисление расстояния Левенштейна между двумя строками
    private static int levenshteinDistance(String s1, String s2) {

        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    public static void main(String[] args) {

        String inputString = "gle.com";

        long start = System.currentTimeMillis();

        String correctWord = suggestCorrection(inputString);

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end-start));

        System.out.println("Введённое слово: " + inputString);
        System.out.println("Нужное слово: " + correctWord);
    }

}
