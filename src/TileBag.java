import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class TileBag {
    private int MAX_NO_TILES;
    private HashMap<String, Integer> LETTER_FREQUENCY;
    private HashMap<String, Integer> LETTER_VALUE;
    private int numTilesLeft;
    private ArrayList<Tile> tiles;

    public TileBag() {
        MAX_NO_TILES = 100;
        LETTER_FREQUENCY = new HashMap<>();
        LETTER_VALUE = new HashMap<>();
        numTilesLeft = 0;
        tiles = new ArrayList<>();

        initializeLetterValues();
    }

    private void initializeLetterValues() {
        // Scrabble-like letter values
        LETTER_VALUE.put("A", 1);
        LETTER_VALUE.put("B", 3);
        LETTER_VALUE.put("C", 3);
        LETTER_VALUE.put("D", 2);
        LETTER_VALUE.put("E", 1);
        LETTER_VALUE.put("F", 4);
        LETTER_VALUE.put("G", 2);
        LETTER_VALUE.put("H", 4);
        LETTER_VALUE.put("I", 1);
        LETTER_VALUE.put("J", 8);
        LETTER_VALUE.put("K", 5);
        LETTER_VALUE.put("L", 1);
        LETTER_VALUE.put("M", 3);
        LETTER_VALUE.put("N", 1);
        LETTER_VALUE.put("O", 1);
        LETTER_VALUE.put("P", 3);
        LETTER_VALUE.put("Q", 10);
        LETTER_VALUE.put("R", 1);
        LETTER_VALUE.put("S", 1);
        LETTER_VALUE.put("T", 1);
        LETTER_VALUE.put("U", 1);
        LETTER_VALUE.put("V", 4);
        LETTER_VALUE.put("W", 4);
        LETTER_VALUE.put("X", 8);
        LETTER_VALUE.put("Y", 4);
        LETTER_VALUE.put("Z", 10);
    }

    public void createTiles() {
        String fileName = "TileBagText.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into letter and frequency
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String letter = parts[0];
                    int frequency = Integer.parseInt(parts[1]);

                    // Get the value for the letter from LETTER_VALUE
                    int value = LETTER_VALUE.getOrDefault(letter, 0);

                    // Create the corresponding number of tiles
                    for (int i = 0; i < frequency; i++) {
                        tiles.add(new Tile(letter, value));
                        numTilesLeft++;
                    }

                    // Update LETTER_FREQUENCY
                    LETTER_FREQUENCY.put(letter, frequency);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Tile> getTilesFromBag(int number) {
        // Logic to get a specific number of tiles (if needed)
        return tiles;
    }

    public int getNumTilesLeft() {
        return numTilesLeft;
    }

    public static void main(String[] args) {
        // Create an instance of TileBag
        TileBag tileBag = new TileBag();

        // Create tiles from the specified text file
        tileBag.createTiles();

        // Write the tiles generated to a file called TileBags.java
        try (PrintWriter writer = new PrintWriter("TileBags.txt")) {
            for (Tile tile : tileBag.getTilesFromBag(tileBag.getNumTilesLeft())) {
                writer.println("Letter: " + tile.getLetter() + ", Value: " + tile.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}