import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;

/**
 * Represents a player in the Scrabble game.
 * Each player has a name, a set of tiles, a score, and access to the tile bag.
 * @author Clinton Odinamadu
 * @author Abdul Babatunde
 * @version October 22, 2024
 */
public class Player implements Serializable {
    // The following are ANSI codes for console text. Might not be portable on Windows computers.
    public static final String RED = "\033[0;31m"; // Red
    public static final String GREEN = "\033[0;32m"; // Green
    public static final String YELLOW = "\033[0;33m"; // Yellow
    public static final String CYAN = "\033[0;36m"; // Cyan

    private static final ArrayList<String> colour = new ArrayList<>(Arrays.asList(YELLOW, RED, GREEN, CYAN));

    /** The maximum amount of tiles a player is allowed to hold*/
    private static final int MAX_TILES = 7;
    /** Tiles*/
    private ArrayList<Tile> tiles;
    /** Player Score*/
    private int score;
    /** Players name*/
    private String name;
    /** The score history of the player*/
    private ArrayList<Integer> scoreHistory;
    /** Letter colour assigned to this player*/
    private String playerColour;

    /**
     * Constructs a new Player with the specified name.
     * Initializes the player's score to 0 and sets up the tile bag and tile list.
     * @param name The name of the player.
     */
    public Player(String name){
        this.name = name;
        scoreHistory= new ArrayList<>();
        score = 0;
        tiles = new ArrayList<>();
        if (!colour.isEmpty()) {
            playerColour = colour.getFirst();
            colour.removeFirst();
        } else playerColour = null;
    }

    /**
     * Gets the name of the player.
     * @return The player's name.
     */
    public String getPlayerName(){
        return name;
    }

    /**
     * Gets the list of tiles currently held by the player.
     * @return An ArrayList of tiles.
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Gets the string representation of the player's tiles.
     * @return The string representation of the player's tiles.
     */
    public String getTilesString() {
        StringBuilder playerTilesStringBuilder = new StringBuilder();
        for (Tile _tile : tiles) {
            playerTilesStringBuilder.append(_tile.getLetter());
        }
        return playerTilesStringBuilder.toString();
    }

    /**
     * Gets the current score of the player.
     * @return The player's score.
     */
    public int getScore(){
        return score;
    }

    /**
     * Gets the colour assigned to this player.
     * @return The colour assigned to this player.
     */
    public String getPlayerColour() {
        return playerColour;
    }

    /**
     * Sets the player's score to the specified value.
     * @param score The new score to be set for the player.
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     * Takes a tile from the players tiles if it matches the letter provided.
     * @param letter The letter provided.
     * @return The tile that matches the letter provided.
     */
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

    /**
     * Clears all the tiles currently held by the player and returns them to the tile bag.
     * @param tileBag The tile bag to which the tiles will be returned.
     */
    public void clearAllTiles(TileBag tileBag) {
        // Iterate through the player's tiles and return them to the tile bag
        for (Tile tile : tiles) {
            tileBag.addTileToBag(tile);  // Add each tile back to the tile bag
        }
        tiles.clear();  // Remove all tiles from the player's list
    }

    /**
     * Returns a tile to the player's tile deck.
     * @param tile The tile to be returned to the tile deck.
     * @return True if the tile could be returned. False otherwise.
     */
    public boolean returnTile(Tile tile) {
        if (tiles.size() < MAX_TILES){
            tiles.add(tile);
            return true;
        }
        return false;
    }

    /**
     * Gives the player a predetermined set of tiles.
     * @param tiles The set of tiles assigned to the player.
     */
    public void setPlayerTiles(ArrayList<Tile> tiles){
        this.tiles = tiles;
        ownTiles();
    }

    /**
     * Get more tiles from the tile bag
     * @param tileBag The tile bag to take the tiles from
     */
    // Modify the method to return a List<Tile> instead of void
    public List<Tile> getMoreTiles(TileBag tileBag) {
        // Draw the tiles and add them to the player's tile list
        List<Tile> drawnTiles = tileBag.getRandomTilesFromBag(MAX_TILES - tiles.size());
        tiles.addAll(drawnTiles);
        ownTiles();

        // Return the drawn tiles
        return drawnTiles;
    }


    /**
     * Transfers ownership of the tile to a player.
     */
    private void ownTiles() {
        for (Tile tile: tiles) {
            tile.setPlayer(this);
        }
    }
    /**
     * Sets a tile at the specified index in the player's tile list.
     * @param index The index at which the tile will be set.
     * @param tile The tile to be set.
     * @return True if the tile was successfully set, false if the index is out of bounds.
     */
    public boolean setTileAtIndex(int index, Tile tile) {
        if (index >= 0 && index < tiles.size()) {
            tiles.set(index, tile);
            return true;  // Tile successfully set
        }
        return false;  // Index is out of bounds
    }

    /**
     * Compares for equality between Players
     * @param obj The object holding the player
     * @return True if the player is the same. False otherwise
     */
    @Override
    public boolean equals(Object obj) {
        Player player = null;
        if (obj instanceof Player) {
            player = (Player) obj;
            return name.equals(player.name);
        }
        if (ScrabbleGame.DEBUG) System.out.printf("Player->equals: Player not equal: %s %s", name, player);
        return false;
    }

    public Tile getTile(int tileIndex) {
        return tiles.get(tileIndex);
    }
}