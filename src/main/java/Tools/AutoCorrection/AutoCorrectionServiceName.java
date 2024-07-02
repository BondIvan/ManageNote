package Tools.AutoCorrection;

import java.util.*;

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
    public static String getOneBestMatch(String inputName, Set<String> dictionary) {

        Map<String, Integer> map = new HashMap<>();

        String lowerCaseInputName = inputName.toLowerCase(); // Нижний регистр чтобы избежать различий в регистре
        int maxSequenceLength = 0;
        for (String candidate : dictionary) {
            int sequenceLength = findMaxSequenceLength(lowerCaseInputName, candidate.toLowerCase());
            map.put(candidate, sequenceLength);
            if (sequenceLength > maxSequenceLength)
                maxSequenceLength = sequenceLength;
        }

        // Найти максимальное значение sequenceLength в карте
        int max = Collections.max(map.values());

        // Создать список с названиями сервисом у которых максимальное значение sequenceLength
        List<String> maxSequenceList = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            if(entry.getValue() == max)
                maxSequenceList.add(entry.getKey());
        }

        String bestMatch = substringSearch(maxSequenceList, lowerCaseInputName);

        return bestMatch;
    }

    // Метод поиска подстроки.
    public static String substringSearch(List<String> serviceList, String input) {

        for (String serviceName : serviceList) {
            if (serviceName.toLowerCase().contains(input))
                return serviceName;
        }

        return null;
    }

    // Список
    public static List<String> getAllBestMatch(String inputName, Set<String> dictionary) {

        Map<String, Integer> map = new HashMap<>();

        String lowerCaseInputName = inputName.toLowerCase(); // Нижний регистр чтобы избежать различий в регистре
        int maxSequenceLength = 0;
        for (String candidate : dictionary) {
            int sequenceLength = findMaxSequenceLength(lowerCaseInputName, candidate.toLowerCase());
            map.put(candidate, sequenceLength);
            if (sequenceLength > maxSequenceLength)
                maxSequenceLength = sequenceLength;
        }

        // Найти максимальное значение sequenceLength в карте
        int max = Collections.max(map.values());

        // Список с названиями сервисом у которых максимальное значение sequenceLength
        List<String> maxSequenceList = map.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .limit(5)
                .toList();

        return maxSequenceList;
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