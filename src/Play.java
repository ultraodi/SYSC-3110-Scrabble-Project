import java.util.ArrayList;

/**
 * Represents a play in the Scrabble game, which consists of a player and a word
 * made up of a list of squares. This class manages the points associated with the play.
 * @author Clinton Odinamadu
 * @author Abdul Babatunde
 * @version October 22, 2024
 */
public class Play {

    /** Represents a player*/
    private Player player;

    /** The generated word of the user*/
    private ArrayList<Square> word;

    /** The letter contributed by player in this play*/
    private ArrayList<Square> contributedLetters;

    /** The points of the current game*/
    private int points;

    /**
     * Constructs a new Play with the specified Player and word.
     * Initializes the points to 0.
     * @param player The player making the play.
     * @param word   An ArrayList of Squares representing the word being played.
     */
    public Play(Player player, ArrayList<Square> word) {
        this.player = player; // Assign the passed Player object
        this.word = word;
        this.contributedLetters = null;
        this.points = 0; // Initialize points, or calculate based on word, etc.
    }

    /**
     * Constructs a new Play with the specified Player and word.
     * Initializes the points to 0.
     * @param player The player making the play.
     * @param word   An ArrayList of Squares representing the word being played.
     */
    public Play(Player player, ArrayList<Square> word, ArrayList<Square> contributedLetters) {
        this.player = player; // Assign the passed Player object
        this.word = word;
        this.contributedLetters = contributedLetters;
        this.points = 0; // Initialize points, or calculate based on word, etc.
    }

    /**
     * Gets the list of squares that contain the word played.
     * @return The list of square containing the word played.
     */
    public ArrayList<Square> getWordSquares() {
        return word;
    }

    /**
     * The string contained of the squares in the play.
     * @return The string representation of the squares in the play.
     */
    public String getWordString() {
        return getSquaresString(word);
    }

    /**
     * Gets the letters the player contributed to the word in this play.
     * @return The letters contributed.
     */
    public ArrayList<Square> getContributedLetters() {
        return contributedLetters;
    }

    public String getContributedLettersString() {
        return getSquaresString(contributedLetters);
    }

    private String getSquaresString(ArrayList<Square> squares) {
        StringBuilder returnString = new StringBuilder();
        Tile tile;
        if (squares == null) return "--No letters--";
        for (Square square : squares) {
            if (square != null) {
                tile = square.getPlacedTile();
                returnString.append(tile.getLetter());
            } else {
                returnString.append("-");
            }
        }
        return returnString.toString();
    }

    /**
     * Sets the word assigned to this play
     * @param word The word assigned to this play.
     */
    public void setWord(ArrayList<Square> word) {
        this.word = word;
    }

    /**
     * Gets the player associated with this play.
     * @return The Player object representing the player making the play.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Gets the points that this play had
     * @return The points this play had
     */
    public int getPoints() {
        return points;
    }

    /**
     * Calculates and updates the player's score based on the points accumulated
     * in this play.
     * @return The updated score of the player after adding the points from this play.
     */
    public int calculatePoints(){
        int currScore = player.getScore() + points;
        player.setScore(currScore);
        return currScore;
    }

    /**
     * Compares Play objects by the words they hold.
     * @param obj The play to be compared against.
     * @return True if they match. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Play) {
            Play play = (Play) obj;
            return getWordString().equals(play.getWordString());
        }
        return false;
    }
}