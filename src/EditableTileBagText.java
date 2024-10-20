import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditableTileBagText {
    private HashMap<String, Integer> letterFrequency;
    private HashMap<String, Integer> letterValue;

    public EditableTileBagText(){
        letterFrequency = new HashMap<>();
        letterValue = new HashMap<>();

        letterFrequency.put("A", 9);
        letterFrequency.put("B", 2);
        letterFrequency.put("C", 2);
        letterFrequency.put("D", 4);
        letterFrequency.put("E", 12);
        letterFrequency.put("F", 2);
        letterFrequency.put("G", 3);
        letterFrequency.put("H", 2);
        letterFrequency.put("I", 9);
        letterFrequency.put("J", 1);
        letterFrequency.put("K", 1);
        letterFrequency.put("L", 4);
        letterFrequency.put("M", 2);
        letterFrequency.put("N", 6);
        letterFrequency.put("O", 8);
        letterFrequency.put("P", 2);
        letterFrequency.put("Q", 1);
        letterFrequency.put("R", 6);
        letterFrequency.put("S", 4);
        letterFrequency.put("T", 6);
        letterFrequency.put("U", 4);
        letterFrequency.put("V", 2);
        letterFrequency.put("W", 2);
        letterFrequency.put("X", 1);
        letterFrequency.put("Y", 2);
        letterFrequency.put("Z", 1);
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
        EditableTileBagText tileBag = new EditableTileBagText();
        tileBag.setFrequency("A", 10); // Example of editing frequency
        tileBag.setValue("A", 2); // Example of editing value
        tileBag.printTileBagToFile("TileBagText.txt");
    }
}
