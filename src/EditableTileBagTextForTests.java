import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditableTileBagTextForTests {
    private HashMap<String, Integer> letterFrequency;
    private HashMap<String, Integer> letterValue;

    public EditableTileBagTextForTests(){
        letterFrequency = new HashMap<>();
        letterValue = new HashMap<>();
        int FREQUENCY = 30;

        letterFrequency.put("A", FREQUENCY);
        letterFrequency.put("B", FREQUENCY);
        letterFrequency.put("C", FREQUENCY);
        letterFrequency.put("D", FREQUENCY);
        letterFrequency.put("E", FREQUENCY);
        letterFrequency.put("F", FREQUENCY);
        letterFrequency.put("G", FREQUENCY);
        letterFrequency.put("H", FREQUENCY);
        letterFrequency.put("I", FREQUENCY);
        letterFrequency.put("J", FREQUENCY);
        letterFrequency.put("K", FREQUENCY);
        letterFrequency.put("L", FREQUENCY);
        letterFrequency.put("M", FREQUENCY);
        letterFrequency.put("N", FREQUENCY);
        letterFrequency.put("O", FREQUENCY);
        letterFrequency.put("P", FREQUENCY);
        letterFrequency.put("Q", FREQUENCY);
        letterFrequency.put("R", FREQUENCY);
        letterFrequency.put("S", FREQUENCY);
        letterFrequency.put("T", FREQUENCY);
        letterFrequency.put("U", FREQUENCY);
        letterFrequency.put("V", FREQUENCY);
        letterFrequency.put("W", FREQUENCY);
        letterFrequency.put("X", FREQUENCY);
        letterFrequency.put("Y", FREQUENCY);
        letterFrequency.put("Z", FREQUENCY);
        // Add all other letters...

        letterValue.put("A", 1);
        letterValue.put("B", 3);
        letterValue.put("C", 3);
        letterValue.put("D", 2);
        letterValue.put("E", 1);
        letterValue.put("F", 4);
        letterValue.put("G", 2);
        letterValue.put("H", 4);
        letterValue.put("I", 1);
        letterValue.put("J", 8);
        letterValue.put("K", 5);
        letterValue.put("L", 1);
        letterValue.put("M", 3);
        letterValue.put("N", 1);
        letterValue.put("O", 1);
        letterValue.put("P", 3);
        letterValue.put("Q", 10);
        letterValue.put("R", 1);
        letterValue.put("S", 1);
        letterValue.put("T", 1);
        letterValue.put("U", 1);
        letterValue.put("V", 4);
        letterValue.put("W", 4);
        letterValue.put("X", 8);
        letterValue.put("Y", 4);
        letterValue.put("Z", 10);
        // Add all other letter values...
    }

    public void setFrequency(String letter, int frequency) {
        if (letterFrequency.containsKey(letter)) {
            letterFrequency.put(letter, frequency);
        } else {
            System.out.println("Letter not found in the bag.");
        }
    }

    public void setValue(String letter, int value) {
        if (letterValue.containsKey(letter)) {
            letterValue.put(letter, value);
        } else {
            System.out.println("Letter not found in the bag.");
        }
    }

    public void printTileBagToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<String, Integer> entry : letterFrequency.entrySet()) {
                String letter = entry.getKey();
                int frequency = entry.getValue();
                int value = letterValue.get(letter);
                for (int i = 0; i < frequency; i++) {
                    writer.write(letter + ", " + value + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EditableTileBagTextForTests tileBag = new EditableTileBagTextForTests();
        tileBag.printTileBagToFile("resources/TileBagTextForTests.txt");
    }
}
