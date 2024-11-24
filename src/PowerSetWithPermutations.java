import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * The {@code PowerSetWithPermutations} class provides utility methods to:
 *
 *  - Generate the power set (all subsets) of a string.
 *  - Generate permutations of each subset in the power set.
 *  - Filter subsets and permutations to generate valid words (e.g., Scrabble words).
 *
 *
 * This class includes methods for recursive generation of subsets and permutations
 * and integrates with a Scrabble game instance to validate generated words.
 *
 * @author Clinton Odinamadu
 */

public class PowerSetWithPermutations {

    /**
     * Generates the power set of the input string and then computes
     * permutations for each subset in the power set.
     *
     * @param str the input string for which to generate subsets and permutations.
     * @return a list of all permutations of all subsets of the input string.
     */
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

    /**
     * Generates the power set (all subsets) of the given string.
     *
     * @param str the input string for which to generate the power set.
     * @return a set of all subsets of the input string.
     */
    private static Set<String> generatePowerSet(String str) {
        Set<String> result = new HashSet<>();
        generateSubsets(str, 0, "", result);
        return result;
    }

    /**
     * Recursively generates all subsets of the given string.
     *
     * @param str    the input string for which to generate subsets.
     * @param index  the current index being processed.
     * @param current the current subset being built.
     * @param result the set to store the generated subsets.
     */
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

    /**
     * Generates all permutations of a given string.
     *
     * @param str the input string for which to generate permutations.
     * @return a list of all permutations of the input string.
     */
    private static List<String> generatePermutations(String str) {
        List<String> result = new ArrayList<>();
        if (str == null || str.length() == 0) {
            result.add("");  // Empty string is a valid permutation
            return result;
        }
        generatePermutationsHelper(str, "", result);
        return result;
    }

    /**
     * Generates a list of valid words by:
     *   - Creating the power set of the input string.</li>
     *   - Generating permutations of each subset.</li>
     *   - Validating each permutation against a Scrabble game word list.</li>
     *
     * @param str the input string from which to generate words.
     * @return a list of unique, valid words generated from the input string.
     */
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

    /**
     * Helper method for recursively generating permutations.
     *
     * @param str     the remaining characters to permute.
     * @param current the current permutation being built.
     * @param result  the list to store the generated permutations.
     */
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
