import java.util.ArrayList;

/**
 * Represents a play in the Scrabble game, which consists of a player and a word
 * made up of a list of squares. This class manages the points associated with the play.
 */
public class Play {

    // Represents a player
    private Player player;

    // The generated word of the user
    private ArrayList<Square> word;

    // The points of the current game
    private int points;

    /**
     * Constructs a new Play with the specified Player and word.
     * Initializes the points to 0.
     *
     * @param player The player making the play.
     * @param word   An ArrayList of Squares representing the word being played.
     */
    public Play(Player player, ArrayList<Square> word) {
        this.player = player; // Assign the passed Player object
        this.word = word;
        this.points = 0; // Initialize points, or calculate based on word, etc.
    }

    /**
     * Gets the player associated with this play.
     *
     * @return The Player object representing the player making the play.
     */
    public Player getPlayer(){
        return player;
    }

    // public String getWord(){}

    /**
     * Calculates and updates the player's score based on the points accumulated
     * in this play.
     *
     * @return The updated score of the player after adding the points from this play.
     */
    public int calculatePoints(){
        int currScore = player.getScore() + points;
        player.setScore(currScore);
        return currScore;
    }
}