import java.util.ArrayList;

/**
 * Represents a player in the Scrabble game.
 * Each player has a name, a set of tiles, a score, and access to the tile bag.
 * @author Clinton Odinamadu
 * @version October 22, 2024
 */
public class Player {
    private static final int MAX_TILES = 7;
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
     * @param name The name of the player.
     */
    public Player(String name){
        this.name = name;
        scoreHistory= new ArrayList<>();
        score = 0;
        //tilebag = new TileBag();
        tiles = new ArrayList<>();
//        Tile a = new Tile("A", 1);
//        Tile p = new Tile("P", 1);
//        Tile p2 = new Tile("P", 1);
//        Tile l = new Tile("L", 1);
//        Tile e = new Tile("E", 1);
//        Tile m1 = new Tile("M", 1);
//        Tile a1 = new Tile("A", 1);
//        Tile n1 = new Tile("N", 1);
//        Tile g1 = new Tile("G", 1);
//        Tile a2 = new Tile("A", 1);
//        Tile e1 = new Tile("E", 1);
//        Tile e2 = new Tile("E", 1);
//        Tile a3 = new Tile("A", 1);
//        Tile s1 = new Tile("S", 1);
//        Tile y1 = new Tile("Y", 1);
//        tiles.add(a);
//        tiles.add(p);
//        tiles.add(p2);
//        tiles.add(l);
//        tiles.add(e);
//        tiles.add(m1);
//        tiles.add(a1);
//        tiles.add(n1);
//        tiles.add(g1);
//        tiles.add(a2);
//        tiles.add(e1);
//        tiles.add(e2);
//        tiles.add(a3);
//        tiles.add(s1);
//        tiles.add(y1);
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
     * Sets the player's score to the specified value.
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
    }

//    /**
//     * Get more tiles from the tile bag at the start of the game
//     * @param tileBag The tile bag to take the tiles from
//     */
//    public ArrayList<Tile> getMoreTilesStart(TileBag tileBag) {
//        tiles = tileBag.getRandomTilesFromBagStart(MAX_TILES - tiles.size());
//        return tiles;
//
//    }

    /**
     * Get more tiles from the tile bag
     * @param tileBag The tile bag to take the tiles from
     */
    public void getMoreTiles(TileBag tileBag) {
        tiles.addAll(tileBag.getRandomTilesFromBag(MAX_TILES - tiles.size()));
        for (Tile tile: tiles) {
            tile.setPlayer(this);
        }
    }

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
}
