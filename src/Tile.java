/**
 * The Tile class simulates the individual tiles that can be interacted with by the users.
 * @author Clinton Odinamadu
 * @author Abdul Babatunde
 * @author Marvel Adotse-ogah
 * @version October 20, 2024
 */

public class Tile {
    /** Player in possession of the tile. Null represents that the tile is still in the tilebab.*/
    private Player player;
    /** Letter on the tile.*/
    private String letter;
    /** Value on the tile.*/
    private int value;

    /**
     * Constructor for the Tile class
     * */
    public Tile(String letter, int value) {
        this.player = null;
        this.letter = letter.toUpperCase();
        this.value = value;
    }

    /**
     * Gets the player in possession of the tile.
     * @return The player in possession of th tile.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the letter on the tile.
     * @return A String holding the letter on the tile.
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Gets the value of the tile.
     * @return The value of the tile.
     */
    public int getValue(){
        return value;
    }

    /**
     * Changes the player in possession of the tile.
     * @param player The player taking possession of the tile.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Represents that the tile has be put back in the tile bag. No one owns the tile.
     */
    public void unsetPlayer() {
        this.player = null;
    }

    /**
     * Compares tiles for equality based on the letter on them.
     * @param obj The tile to compare with
     * @return True if they have thes same letters. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile t = (Tile) obj;
            return letter.equals(t.letter);
        }
        return false;
    }
}
