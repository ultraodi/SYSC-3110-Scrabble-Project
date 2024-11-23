import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

public class PowerSetWithPermutations {

    // Function to generate power set and permutations of each subset
    private static List<String> generatePowerSetWithPermutations(String str) {
        List<String> result = new ArrayList<>();
        Set<String> powerSet = generatePowerSet(str);

        // For each subset in the power set, generate permutations
        for (String subset : powerSet) {
            List<String> permutations = generatePermutations(subset);
            result.addAll(permutations);  // Add all permutations of the subset
        }

        return result;
    }

    // Function to generate power set of a string (all subsets)
    private static Set<String> generatePowerSet(String str) {
        Set<String> result = new HashSet<>();
        generateSubsets(str, 0, "", result);
        return result;
    }

    // Recursive function to generate all subsets (power set)
    private static void generateSubsets(String str, int index, String current, Set<String> result) {
        if (index == str.length()) {
            result.add(current);  // Add the current subset to the result
            return;
        }

        // Include the current character
        generateSubsets(str, index + 1, current + str.charAt(index), result);
        // Exclude the current character
        generateSubsets(str, index + 1, current, result);
    }

    // Function to generate all permutations of a string
    private static List<String> generatePermutations(String str) {
        List<String> result = new ArrayList<>();
        if (str == null || str.length() == 0) {
            result.add("");  // Empty string is a valid permutation
            return result;
        }
        generatePermutationsHelper(str, "", result);
        return result;
    }

    // Function to generate all valid words
    public static List<String> generateValidWords(String str) {
        ScrabbleGame game = new ScrabbleGame();
        List<String> allPossibleWords = generatePowerSetWithPermutations(str);

        // Use a LinkedHashSet to ensure uniqueness while preserving order
        Set<String> validWords = new LinkedHashSet<>();

        for (String possWord : allPossibleWords) {
            if (game.isValidWord(possWord)) {
                validWords.add(possWord); // Add only valid words (duplicates automatically handled)
            }
        }

        // Convert back to a list if needed
        return new ArrayList<>(validWords);
    }

    // Helper recursive function to generate permutations
    private static void generatePermutationsHelper(String str, String current, List<String> result) {
        if (str.length() == 0) {
            result.add(current);  // If string is empty, add current permutation
        }
        for (int i = 0; i < str.length(); i++) {
            String newStr = str.substring(0, i) + str.substring(i + 1);
            generatePermutationsHelper(newStr, current + str.charAt(i), result);
        }
    }
}
