import java.util.ArrayList;

/**
 * The ScrabbleGame class simulates as at this version a turn by turn text version of the
 * game Scrabble.
 * @author Clinton Odinamadu, Abdul Babatunde, Marvel Adotse-ogah
 * @version October 19, 2024
 */
public class ScrabbleGame {
    /** Players of the game*/
    private ArrayList<Player> players;
    /** Holds current player's turn*/
    private Player playerTurn;
    /** The scrabble board for the game*/
    private ScrabbleBoard scrabbleBoard;
    /** The tile bag for the game*/
    private TileBag tileBag;
    /** Holds the history of the successful words made by the players*/
    private ArrayList<Play> plays;

    /**
     * Constructor for the ScrabbleGame class.
     * @param numPlayers The number of players in the game.
     */
    public ScrabbleGame(int numPlayers) {
        players = new ArrayList<>();
        scrabbleBoard = new ScrabbleBoard();
        tileBag = new TileBag();
        plays = new ArrayList<>();

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }
    }

    /**
     * Initiates a new game of Scrabble.
     */
    public void game() {

    }

    /**
     * Changes over to the appropriate player's turn.
     */
    public void changeTurns() {

    }

    /**
     * Plays the player's word on the game board if it is valid.
     * @return True if the play could be made. False if the play would not be made.
     */
    public boolean playWord() {
        return true;
    }

    public static void main(String[] args) {

    }
}
