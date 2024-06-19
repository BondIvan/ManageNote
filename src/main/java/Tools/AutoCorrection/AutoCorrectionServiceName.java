package Tools.AutoCorrection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AutoCorrectionServiceName {

    // Словарь не должен содержать дубликаты слов, поэтому используется Set
    // Также можно использовать HashMap<String, Integer> - Integer будет отвечать за дополнительную информацию о словах (частота встречи и т.п.),
    // по эмолчанию 1. Exp: dictionary.put("Google.com", 1);

//    Example: delte gogle --->  delete google.com
//    public static String autoCorrectFewWords(String input) {
//        String[] words = input.split(" ");
//        StringBuilder correctedText = new StringBuilder();
//
//        for (String word : words) {
//            String correctedWord = findBestCorrection(word);
//            correctedText.append(correctedWord).append(" ");
//        }
//
//        return correctedText.toString().trim();
//    }

    // Сравнение максимальной последовательности искомого слова с каждым словом из словаря
    public static String getOneBestMatch(String serviceName, Set<String> dictionary) {

        long start = System.currentTimeMillis();

        String bestMatch = serviceName;
        int maxSequenceLength = 0;

        for (String candidate : dictionary) {
            int sequenceLength = findMaxSequenceLength(serviceName, candidate);
            if (sequenceLength > maxSequenceLength) {
                maxSequenceLength = sequenceLength;
                bestMatch = candidate;
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("One: " + (end-start));

        return bestMatch;
    }

    public static List<String> getThreeBestMatch(String serviceName, Set<String> dictionary) {

        long start = System.currentTimeMillis();

        List<String> threeMatch = new ArrayList<>();

        int maxSequenceLength = 0;
        String cycleBestMatch = "";
        for(int i = 0; i < 3; i++) {
            for (String candidate : dictionary) {
                if(threeMatch.contains(candidate))
                    continue;

                int currSequenceLength = findMaxSequenceLength(serviceName, candidate);
                if(currSequenceLength > maxSequenceLength) {
                    maxSequenceLength = currSequenceLength;
                    cycleBestMatch = candidate;
                }
            }

            threeMatch.add(cycleBestMatch);
            cycleBestMatch = "";
            maxSequenceLength = 0;
        }

        long end = System.currentTimeMillis();

        System.out.println("Three: " + (end-start));

        return threeMatch;
    }

    // Метод: поиск максимальной последовательности
    private static int findMaxSequenceLength(String serviceName, String candidate) {
        int[][] dp = new int[serviceName.length() + 1][candidate.length() + 1];

        for (int i = 1; i <= serviceName.length(); i++) {
            for (int j = 1; j <= candidate.length(); j++) {
                if (serviceName.charAt(i - 1) == candidate.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[serviceName.length()][candidate.length()];
    }


}