import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.util.Random;

public class TileBag {
    private int MAX_NO_TILES;
    private HashMap<String, Integer> LETTER_FREQUENCY;
    private HashMap<String, Integer> LETTER_VALUE;
    private int numTilesLeft;
    private ArrayList<Tile> tiles;

    public TileBag() {
        MAX_NO_TILES = 100; // Set the max number of tiles allowed in the tile bag
        LETTER_FREQUENCY = new HashMap<>();
        LETTER_VALUE = new HashMap<>();
        numTilesLeft = 0;
        tiles = new ArrayList<>();

        // Initialize the letter value (based on Scrabble values or your custom logic)
        initializeLetterValues();
    }

    private void initializeLetterValues() {
        // Example Scrabble-like letter values
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
        String fileName = "TileBagText.txt"; // Adjust the file path accordingly

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into letter and value directly
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String letter = parts[0];
                    int value = Integer.parseInt(parts[1]);

                    // Create the tile for this line
                    tiles.add(new Tile(letter, value));
                    numTilesLeft++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Tile> getRandomTilesFromBag(int number) {
        // Logic to get a specific number of tiles (if needed)
        ArrayList<Tile> randomTiles = new ArrayList<>();
        Random rand = new Random();
        int maxNumber = getNumTilesLeft();

        // Ensure the number of tiles requested is not greater than the available tiles
        if (number > maxNumber){
            throw new IllegalArgumentException("Bound must be positive");
        }

        for (int i = 0; i < number; i++) {
            // Get a random index within the current size of the tiles list
            int randomIndex = rand.nextInt(tiles.size());

            // Add the tile at the random index to the randomTiles list
            randomTiles.add(tiles.get(randomIndex));

            // Remove the tile from the tiles list to avoid duplication
            tiles.remove(randomIndex);

            // Decrements the remaining tile counter
            numTilesLeft--;
        }

        return randomTiles;
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

        // Write the tiles to "TileBags.txt"
        try (FileWriter writer = new FileWriter("TileBags.txt")) {
            // Write the number of tiles left to the file
            writer.write("Number of tiles left: " + tileBag.getNumTilesLeft() + "\n");

            for (Tile tile : tileBag.getTilesFromBag(tileBag.getNumTilesLeft())) {
                writer.write("Letter: " + tile.getLetter() + ", Value: " + tile.getValue() + "\n");
            }
            // Get random tiles (e.g., 5 random tiles) and write them to the file
            ArrayList<Tile> randomTiles = tileBag.getRandomTilesFromBag(97);
            writer.write("\nRandomly selected tiles:\n");
            for (Tile tile : randomTiles) {
                writer.write("Letter: " + tile.getLetter() + ", Value: " + tile.getValue() + "\n");
            }
            // Write the number of tiles left to the file
            writer.write("Number of tiles left: " + tileBag.getNumTilesLeft() + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
