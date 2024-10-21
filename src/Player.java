import java.util.ArrayList;

/**
 * Represents a player in the Scrabble game.
 * Each player has a name, a set of tiles, a score, and access to the tile bag.
 */
public class Player {

    // Tile Bag
    private TileBag tilebag;

    // Tiles
    private ArrayList<Tile> tiles;

    // Player Score
    private int score;

    // Players name
    private String name;

    private ArrayList<Integer> scoreHistory;

    /**
     * Constructs a new Player with the specified name.
     * Initializes the player's score to 0 and sets up the tile bag and tile list.
     *
     * @param name The name of the player.
     */
    public Player(String name){
        this.name = name;
        scoreHistory= new ArrayList<>();
        score = 0;
        tilebag = new TileBag();
        tiles = new ArrayList<>();
        Tile a = new Tile("A", 1);
        Tile p = new Tile("P", 1);
        Tile p2 = new Tile("P", 1);
        Tile l = new Tile("L", 1);
        Tile e = new Tile("E", 1);
        tiles.add(a);
        tiles.add(p);
        tiles.add(p2);
        tiles.add(l);
        tiles.add(e);
    }

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getPlayerName(){
        return name;
    }

    /**
     * Gets the list of tiles currently held by the player.
     *
     * @return An ArrayList of tiles.
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Gets the current score of the player.
     *
     * @return The player's score.
     */
    public int getScore(){
        return score;
    }

    /**
     * Sets the player's score to the specified value.
     *
     * @param score The new score to be set for the player.
     */
    public void setScore(int score){
        this.score = score;
    }

    public Tile takeTile(String letter) {
        Tile tile = new Tile(letter, 1);
        Tile returnTile;
        if (tiles.contains(tile)) {
            returnTile = tiles.get(tiles.indexOf(tile));
            tiles.remove(tile);
            return returnTile;
        }
        return null;
    }

    //
    // players move
    //public ArrayList<Tile> playersMove(){}

    // get more tiles
    // public void getMoreTiles() {}

    //    public ArrayList<Tile> getTiles() {
    //        Tile a = new Tile("A", 1);
    //        Tile p = new Tile("P", 1);
    //        Tile p2 = new Tile("L", 1);
    //        Tile l = new Tile("L", 1);
    //        Tile e = new Tile("E", 1);
    //        ArrayList<Tile> tiles = new ArrayList<>();
    //        tiles.add(a);
    //        tiles.add(p);
    //        tiles.add(p2);
    //        tiles.add(l);
    //        tiles.add(e);
    //        return  tiles;
    //    }
}
