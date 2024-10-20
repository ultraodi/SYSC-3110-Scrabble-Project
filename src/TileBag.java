import java.util.ArrayList;
import java.util.HashMap;

public class TileBag {
    private int MAX_NO_TILES;
    private HashMap<String, Integer> LETTER_FREQUENCY;
    private HashMap<String, Integer> LETTER_VALUE;
    private int numTilesLeft;
    private ArrayList<Tile>  tiles;


    public TileBag(){
            MAX_NO_TILES = 100; // Example value, adjust as needed
            LETTER_FREQUENCY = new HashMap<>();
            LETTER_VALUE = new HashMap<>();
            numTilesLeft = 0;
            tiles = new ArrayList<>();

            // Add logic to initialize LETTER_FREQUENCY, LETTER_VALUE, and tiles
    }

    public ArrayList<Tile> getTilesFromBag(int number){
        return tiles;
    }

    public void createTiles(){

    }
}
