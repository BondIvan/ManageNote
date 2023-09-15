package Tools.AutoCorrection;

import Entity.NoteEntity;
import Source.StartConsole;
import Tools.UsefulMethods;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AutoCorrectionServiceName {

    // Словарь не должен содержать дубликаты слов, поэтому используется Set
    private static Set<String> dictionary;

    public static void main(String[] args) throws IOException {

        List<NoteEntity> notes = UsefulMethods.getAllNoteFromFile(StartConsole.PATH_TEST);
        List<String> dic = UsefulMethods.getAllUniqueServiceName(notes);

        dictionary = new HashSet<>(dic);

        String inputWithError = "jacdoe";

        long start = System.currentTimeMillis();

        autoCorrect(inputWithError);

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end-start));

        System.out.println(dic);

//        System.out.println("Введённое: " + inputWithError);
//        System.out.println("Верное: " + autoCorrect(inputWithError));

    }

    public static String autoCorrect(String input) {
        String[] words = input.split(" ");
        StringBuilder correctedText = new StringBuilder();

        for (String word : words) {
            String correctedWord = findBestCorrection(word);
            correctedText.append(correctedWord).append(" ");
        }

        return correctedText.toString().trim();
    }

    private static String findBestCorrection(String word) {
        String bestMatch = word;
        int maxSequenceLength = 0;

        for (String candidate : dictionary) {
            int sequenceLength = findMaxSequenceLength(word, candidate);
            if (sequenceLength > maxSequenceLength) {
                maxSequenceLength = sequenceLength;
                bestMatch = candidate;
            }
        }

        return bestMatch;
    }

    private static int findMaxSequenceLength(String word, String candidate) {
        int[][] dp = new int[word.length() + 1][candidate.length() + 1];

        for (int i = 1; i <= word.length(); i++) {
            for (int j = 1; j <= candidate.length(); j++) {
                if (word.charAt(i - 1) == candidate.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[word.length()][candidate.length()];
    }


}