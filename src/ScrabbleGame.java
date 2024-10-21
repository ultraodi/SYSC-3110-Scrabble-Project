import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static java.lang.String.*;

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
    /** URL string*/
    private final String validWordsURLString = "https://www.mit.edu/~ecprice/wordlist.10000";
    /** Holds the file path for the text file containing the valid words*/
    private final String validWordsFile = "valid_words.txt";
    /** Holds a set of valid words*/
    private HashSet<String> validWords;
    /** Holds the current word played*/
    private String currentValidWord;
    /** Input prompt for the user interface*/
    private final String inputPrompt = "Word to play >> ";
    /** Player input for each play*/
    private String playerInput;
    /** Word to play in players' input*/
    private String playerInputWord;
    /** Board Position to place the played word*/
    private String playerInputBoardPosition;
    /** Possible play directions*/
    private enum Direction {HORIZONTAL, VERTICAL}
    /** Play direction of a played word either horizontal or vertical*/
    private Direction playerInputPlayDirection;

    /**
     * Constructor for the ScrabbleGame class.
     * @param numPlayers The number of players in the game.
     */
    public ScrabbleGame(int numPlayers) {
        players = new ArrayList<>();
        scrabbleBoard = new ScrabbleBoard();
        tileBag = new TileBag();
        plays = new ArrayList<>();
        playerTurn = null;
        // The URL class throws the MalformedURLException so the program is required to handle it.
        try {
            validWordsURL = new URL(validWordsURLString);
            validWords = getValidWordsFromUrl();
        } catch (Exception e) {
            validWords = getValidWordsFromFile();
        }
        validWords = getValidWordsFromFile();
        currentValidWord = "";
        playerInputWord = "";
        playerInputBoardPosition = "";
        playerInputPlayDirection = Direction.HORIZONTAL;
        playerTurn = new Player("Jack");

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("John Doe " + i));
        }
    }

    /**
     * Initiates a new game of Scrabble.
     */
    public void game() {
        /*
         * Game flow
         * - Program decides who goes first by randomly drawing one tile from the bag for the players
         *  - The player that is closest to the letter A goes first
         * - Player makes a play given the tiles they have
         *  - If the play is invalid, they are prompted again
         *      - A play is invalid if:
         *          - There is not enough space in the direction they want to play
         *          - They do not have the required tiles
         *          - One or more of the words formed by their play is not a valid word
         *          - The play cannot be made if the tiles on the board, that are in the direction they chose to play, do not fit the word played. E.g. APPLE is played but the board has something
         *            like -- |   |   | P | E  |   |   |   | --, apple will not be able to be played with those letters.
         *  - The tiles a player has will be given above the input area
         *  - If the play is valid, the player's tiles are automatically restocked/replaced
         * - Points are calculated and the player's score updated
         * - Play(s) are recorded.
         * - Switches player
         * - Repeats until one of the following conditions is met:
         *  - There are no tiles left in the tile bag
         *  - No players can make a play after 6 turns
         */
        scrabbleBoard.drawBoard();
        ArrayList<Player> playerOrder = determinePlayerTurns();
        while (true) {
            getInputPlay();
            if (parseInputPlay()) {
                if (isValidWord(playerInputWord)) {
                    System.out.printf("Word: %s | Position: %s | Direction: %s%n", playerInputWord, playerInputBoardPosition, playerInputPlayDirection.equals(Direction.HORIZONTAL)? "Horizontal":"Vertical");
                    if (playWord()) {
                        scrabbleBoard.drawBoard();
                    } else {
                        System.out.println("Couldn't play word");
                    }
                } else {
                    System.out.println("Valid Word it is not.");
                }
            } else {
                System.out.println("Not a valid Entry");
            }
        }
    }

    /**
     * Changes over to the appropriate player's turn.
     */
    public void changeTurns() {

    }

    /**
     * Plays the player's word on the game board if it is valid.
     * @return True if the play could be made. False if the play could not be made.
     */
    public boolean playWord() {
        /*
        * To implement this, initial design is to go through:
        * - Checking if making the play is possible.
        *   - Are there enough squares to play the word?
        *   - The letters on the board in the area of play are in the word played
        *   - The letters on the board in the area of play are already in the desired position
        *   - Does the player making the play have the needed tiles to complete the word given the
        *     letters already on the board?
        *   - All words formed as a result of the played word are valid
        * - Making the play.
        *   - Taking the necessary letters from the player's tile deck when needed
        *
        * If any of the checks fail, the play cannot be made.
        */

        // Are there enough squares to play the word
        Direction dir = playerInputPlayDirection;
        int rowNum = Integer.parseInt(valueOf(playerInputBoardPosition.charAt(0)), 16);
        int colNum = Integer.parseInt(valueOf(playerInputBoardPosition.charAt(1)), 16);
        if (!((15 - (dir.equals(Direction.VERTICAL)? colNum:rowNum) + 1) >= playerInputWord.length())) {
            System.out.println(String.format("ScrabbleGame->playWord: Word is too long. row: %d col: %d word length: %d", rowNum, colNum, playerInputWord.length()));
            return false;
        }

        // The letters on the board in the area of play are in the word played
        ArrayList<Tile> lettersOnTheBoard = new ArrayList<>();
        int row;
        int col;
        Tile tile;
        for (int i = 0; i < playerInputWord.length(); i++) {
            row = dir.equals(Direction.HORIZONTAL)? rowNum + i:rowNum;
            col = dir.equals(Direction.VERTICAL)? colNum + i:colNum;
            tile = scrabbleBoard.getTileOnSquare(row, col);
            if (tile != null) {
                lettersOnTheBoard.add(tile);
            } else {
                lettersOnTheBoard.add(null);
            }
        }
        for (Tile letter:lettersOnTheBoard) {
            if (letter != null) {
                if (!playerInputWord.contains(letter.getLetter())) {
                    System.out.println("ScrabbleGame->playWord: Letters on the board are not in the word played");
                    return false;
                }
            }
        }

        // The letters on the board in the area of play are already in the desired position
        String dupPlayerInputWord = "".concat(playerInputWord);
        for (Tile letter:lettersOnTheBoard) {
            if (letter != null) {
                if (dupPlayerInputWord.indexOf(letter.getLetter()) != lettersOnTheBoard.indexOf(letter)) {
                    System.out.println("ScrabbleGame->playWord: Letters on the board are not in the correct position for the word played");
                    return false;
                }
                dupPlayerInputWord = dupPlayerInputWord.replaceFirst(letter.getLetter(), "-");
            }
        }

        // Does the player making the play have the needed tiles to complete the word given the
        // letters already on the board?
        String playerTiles;
        StringBuilder playerTilesStringBuilder = new StringBuilder();

        for (Tile _tile :playerTurn.getTiles()) {
            playerTilesStringBuilder.append(_tile.getLetter());
        }
        playerTiles = playerTilesStringBuilder.toString();
        System.out.println("ScrabbleGame->playWord: Player's Tiles: " + playerTiles);
        System.out.println("ScrabbleGame->playWord: Word played: " + dupPlayerInputWord);
        System.out.print("ScrabbleGame->playWord: ");
        for (int i = 0; i < dupPlayerInputWord.length(); i++) {
            if (dupPlayerInputWord.charAt(i) != '-') {
                System.out.print(dupPlayerInputWord.charAt(i));
                if (playerTiles.contains(valueOf(dupPlayerInputWord.charAt(i)).toUpperCase())) {
                    playerTiles = playerTiles.replaceFirst(valueOf(dupPlayerInputWord.charAt(i)), "-");
                } else {
                    System.out.println("\nScrabbleGame->playWord: Player does not have the needed tiles");
                    return false;
                }
            }
        }
        System.out.print("\n");

        // All words formed as a result of the played word are valid
        // Create list of plays
        // Iterate over list and check all words are valid
            // Check if the play already exists and remove it from the list of plays if it does.
        //

        putPlayOnBoard(dir, rowNum, colNum);
        return true;
    }

    /*public boolean allPlayWordsValid(Direction dir, int rowNum, int colNum, String word) {
        int row, col, row2, col2;
        ArrayList<Square> tileWord = null;
        ArrayList<Play> plays;
        for (int i = 0; i < word.length(); i++) {
            row = dir.equals(Direction.HORIZONTAL)? rowNum:rowNum + i;
            col = dir.equals(Direction.VERTICAL)? colNum:colNum + i;

            if (dir.equals(Direction.VERTICAL)) {

            } else if (dir.equals(Direction.HORIZONTAL)) {
                row2 = rowNum + i;
                col2 = colNum;
                while (scrabbleBoard.getSquare(row2 - 1, col2).getPlacedTile() != null) {
                    row2--;
                }
                while (scrabbleBoard.getSquare(row2 + 1, col2).getPlacedTile() != null) {
                    if (tileWord != null) {
                        tileWord.add(scrabbleBoard.getSquare(row2, col2));
                    }
                    row2++;
                }
                plays.add(new Play("nil", tileWord));
            }
        }
        return true;
    }*/

    public void putPlayOnBoard(Direction dir, int rowNum, int colNum) {
        // Making the play
        // Taking the necessary letters from the player's tile deck when needed
        Tile playTile;
        int row, col;
        for (int i = 0; i < playerInputWord.length(); i++) {
            row = dir.equals(Direction.HORIZONTAL)? rowNum:rowNum + i;
            col = dir.equals(Direction.VERTICAL)? colNum:colNum + i;
            ArrayList<Tile> playerTurnTiles = playerTurn.getTiles();
            if (scrabbleBoard.getSquare(row - 1, col - 1).getPlacedTile() == null) {
                playTile = playerTurn.takeTile(valueOf(playerInputWord.charAt(i)).toUpperCase());
                scrabbleBoard.placeTile(row - 1, col - 1, playTile);
            } else continue;
        }
    }

    public void removePlayFromBoard(Play play) {

    }

    /**
     * Gets the order of play for the players.
     * @return The order of play for the players.
     */
    public ArrayList<Player> determinePlayerTurns() {
        ArrayList<Player> returnTurns = new ArrayList<>();
        ArrayList<Player> dupPlayers = players;
        ArrayList<String> playerDraws = new ArrayList<>();
        ArrayList<String> sortedPlayerDraws = new ArrayList<>();
        ArrayList<Tile> tile;
        for (int i = 0; i < players.size(); i++) {
            tile = tileBag.getRandomTilesFromBag(1);
            playerDraws.add(tile.get(0).getLetter());
        }

        int lowestIndex = 0;
        String minChar;
        for (Player player: players) {
            minChar = playerDraws.getFirst();
            lowestIndex = 0;
            int i;
            for (i = 0; i < players.size(); i++) {
                if (minChar.charAt(0) > playerDraws.get(i).charAt(0)) {
                    minChar = playerDraws.get(i);
                    lowestIndex = i;
                }
            }
            sortedPlayerDraws.add(minChar);
            playerDraws.remove(lowestIndex);
            playerDraws.add(lowestIndex, "a");
            returnTurns.add(players.get(lowestIndex));
        }

        for (int j = 0; j < returnTurns.size(); j++) {
            System.out.println(String.format("Player: %s Tile Drawn: %s", returnTurns.get(j).getPlayerName(), sortedPlayerDraws.get(j)));
        }
        return returnTurns;
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

    /**
     * Gets the players' input
     */
    private void getInputPlay() {
        Scanner inputWord = new Scanner(System.in);
        System.out.print(inputPrompt);
        playerInput = inputWord.nextLine();
    }

    /**
     * Parses through the user's input and checks for errors. It updates the playerInputWord, playerInputBoardPosition, and playerInputPlayDirection fields.
     * @return Returns true if both word and board position parameters are valid. Returns false if board position is out of range.
     */
    private boolean parseInputPlay() {
        String _tempWord = "";
        String _tempPosition = "";
        String _tempDirection = "";
        String row = "";
        String col = "";
        Scanner tokenizer = new Scanner(playerInput);
        if (tokenizer.hasNext()) { // Checks that the play has a word
            _tempWord = tokenizer.next(); // This allows any type of input be it numbers or special characters. A String of characters
                                          // containing numbers or special characters will not be considered a valid word.
        } else return false;
        if (tokenizer.hasNext()) { // Checks that they play has coordinates
            _tempPosition = tokenizer.next();
            if (_tempPosition.length() != 2) { // Checks that the coordinates given are exactly 2 (e.g. ff)
                return false;
            }
            row = valueOf(_tempPosition.charAt(0));
            col = valueOf(_tempPosition.charAt(1));
            try {
                if (!((Integer.parseInt(row, 16) > 0) && (Integer.parseInt(col, 16) > 0))) { // Checks that the converted number is within the range of 1 to 15 (0xF)
                    return false;
                }
                Integer.parseInt(row, 16); // Attempts to convert to Decimal and gives an error if unsuccessful
                Integer.parseInt(col, 16);
            } catch (Exception e) {return false;} // Any issues with exceptions will result in the play made invalid
        } else return false;
        if (tokenizer.hasNext()) {
            _tempDirection = tokenizer.next().toLowerCase();
            String dir = valueOf(_tempDirection.charAt(0)); // First letter of the argument in the parameter for direction so v, vertical, and any word starting with v would work.
            // A similar logic applies for h and horizontal
            if (dir.equals("h") || dir.equals("v")) {
                _tempDirection = dir;
            } else return false;
        } else return false;

        playerInputWord = _tempWord.toLowerCase();
        playerInputBoardPosition = _tempPosition.toUpperCase();
        playerInputPlayDirection = (_tempDirection.equals("h"))? Direction.HORIZONTAL : Direction.VERTICAL;
        return true;
    }

    /**
     * Determines if a word is in the list of valid words.
     * @param word The word to check
     * @return true if the word is valid. false otherwise.
     */
    private boolean isValidWord(String word) {
        if (word == null) {return false;}
        return validWords.contains(word.toLowerCase());
    }

    public static void main(String[] args) {
        ScrabbleGame game = new ScrabbleGame(4);
        game.game();
    }
}
