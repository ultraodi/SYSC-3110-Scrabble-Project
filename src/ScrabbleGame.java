import java.io.*;
import java.net.HttpURLConnection;
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
    private URL validWordsURL;
    /** Holds the file path for the text file containing the valid words*/
    private final String validWordsFile = "valid_words.txt";
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
        // The URL class throws the MalformedURLException so the program is required to handle it.
        try {
            validWordsURL = new URL("https://www.mit.edu/~ecprice/wordlist.10000");
            validWords = getValidWordsFromUrl();
        } catch (Exception e) {
            validWords = getValidWordsFromFile();
        }
        validWords = getValidWordsFromFile();
        /*String name = "";
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }*/
        System.out.println(getInputPlay());
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

    /**
     * Retrieves a set of valid words from the url given in validWordsURL.
     * This function was mostly to practice the HttpURLConnection, BufferedReader, and Scanner classes.
     * @return A HashSet of the valid words from the validWordsURL
     */
    private HashSet<String> getValidWordsFromUrl() {
        HashSet<String> returnSet = new HashSet<>();
        BufferedReader inStream; // The BufferedReader class simulates a buffer that allows a stream of
                                 // input to be read more efficiently.
                                 // It reads larger amounts of data from the input stream rather than byte by byte.
        StringBuilder validWordsString = new StringBuilder(); // To build a string of valid words separated by new lines.
        try {
            HttpURLConnection connection = (HttpURLConnection) validWordsURL.openConnection();
            connection.setRequestMethod("GET"); // Configures the connection's type of request. In this case, it is the GET request
            inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inStreamLine;
            while ((inStreamLine = inStream.readLine()) != null) {
                validWordsString.append(inStreamLine.toLowerCase()).append("\n"); // Adds a new line to each word as the 'inStream.readLine()'
                                                                            // in the line above removes the trailing new line by default

            }

            inStream.close(); // Closes the simulated buffer
            connection.disconnect(); // Closes the url connection

            Scanner validWords = new Scanner(validWordsString.toString()); // Allows the string built by the validWordsString to be tokenized (read word by word in this case)

            while (validWords.hasNext()) {
                returnSet.add(validWords.next().strip()); // Adds all the words to a hash set and removes the added new lines. The new lines were needed in order for the
                                                          // Scanner class to parse through the string for tokens.
            }
        } catch (IOException e) { // The .openConnection() method in the HttpURLConnection class throws an IOException so our program needs to handle it.
            throw new RuntimeException(e);
        }
        return returnSet;
    }

    /**
     * Retrieves a set of valid words from the file given in validWordsFile.
     * @return A HashSet of the valid words from the validWordsFile.
     */
    private HashSet<String> getValidWordsFromFile() {
        HashSet<String> returnSet = new HashSet<>();
        try {
            BufferedReader content = new BufferedReader(new FileReader(validWordsFile));
            String contentLine;
            while ((contentLine = content.readLine()) != null) {
                returnSet.add(contentLine.strip()); // Adds the valid words to a set
            }
        } catch (FileNotFoundException e) { // Program needs to handle the exception thrown by the FileReader class.
            System.out.println("Could not find file: " + validWordsFile);
        } catch (IOException e) { // Program needs to handle the exception thrown by the readLine() method in BufferReader
            System.out.println("Could not complete IO operations on " + validWordsFile);
        }
        return returnSet;
    }

    private String getInputPlay() {
        Scanner inputWord = new Scanner(System.in);
        System.out.print("Word to play >> ");
        return inputWord.nextLine();
    }

    public static void main(String[] args) {
        ScrabbleGame game = new ScrabbleGame(2);
    }
}
