import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * The ScrabbleGame class simulates as at this version a turn by turn text version of the
 * game Scrabble.
 * @author Clinton Odinamadu
 * @author Abdul Babatunde
 * @author Marvel Adotse-ogah
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
    /** Holds the URL from which the validWords set will be generated from*/
    private final URL validWordsURL;
    /** Holds a set of valid words*/
    private HashSet<String> validWords;
    /** Holds the current word played*/
    private String currentWord;

    /**
     * Constructor for the ScrabbleGame class.
     * @param numPlayers The number of players in the game.
     */
    public ScrabbleGame(int numPlayers) {
        players = new ArrayList<>();
        scrabbleBoard = new ScrabbleBoard();
        tileBag = new TileBag();
        plays = new ArrayList<>();
        try {
            validWordsURL = new URL("https://www.mit.edu/~ecprice/wordlist.10000");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        validWords = getValidWordsFromUrl();
        List<String> _list = new ArrayList<String>(validWords);
        Collections.sort(_list);
        System.out.println(_list);
        System.out.println(_list.size());

        /*String name = "";
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }*/
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

    private HashSet<String> getValidWordsFromUrl() {
        HashSet<String> returnSet = new HashSet<>();
        BufferedReader inStream;
        StringBuilder validWordsString = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) validWordsURL.openConnection();
            // Configures the connection's type of request. In this case, it is the GET request
            connection.setRequestMethod("GET");
            inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inStreamLine;
            while ((inStreamLine = inStream.readLine()) != null) {
                validWordsString.append(inStreamLine.toLowerCase() + "\n");
            }

            inStream.close();
            connection.disconnect();

            Scanner validWords = new Scanner(validWordsString.toString());

            while (validWords.hasNext()) {
                returnSet.add(validWords.next().strip());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return returnSet;
    }

    public static void main(String[] args) {
        ScrabbleGame game = new ScrabbleGame(2);
    }
}
