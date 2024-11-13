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
 * @version October 22, 2024
 */
public class ScrabbleGame {
    public static boolean DEBUG = false; // To be added to the XML 'configuration' file
    public static boolean TEXT_VERSION = false;
    /** Players of the game*/
    private ArrayList<Player> players;
    /** Holds current player's turn*/
    private Player playerTurn;
    /** The scrabble board for the game*/
    private final ScrabbleBoard scrabbleBoard;
    /** The tile bag for the game*/
    private TileBag tileBag;
    /** Holds the history of the successful words made by the players*/
    private ArrayList<Play> plays;
    /** Holds the URL from which the validWords set will be generated from*/
    private URL validWordsURL;
    /** Holds a set of valid words*/
    private HashSet<String> validWords;
    /** Player input for each play*/
    private String playerInput;
    /** Word to play in players' input*/
    private String playerInputWord;
    /** Board Position to place the played word*/
    private String playerInputBoardPosition;
    /** Possible play directions*/
    public enum Direction {HORIZONTAL, VERTICAL}
    /** Play direction of a played word either horizontal or vertical*/
    private Direction playerInputPlayDirection;
    /** Holds a list of the player's order*/
    private ArrayList<Player> playerOrder;
    /** Holds the current index of the player's turn */
    private int currentPlayerIndex = 0;
    /** Current play being worked with*/
    private Play currentPlay;
    /** Is true when the player's input is a command. False otherwise*/
    private boolean isCommand;
    /** Stores the command*/
    private String command;
    /** Player Coordinates*/
    int rowNum_program;
    int colNum_program;
    /** Valid commands for the game*/
    private static final ArrayList<String> commands = new ArrayList<>(List.of(new String[]{"-debug", "-noDebug", "-board", "-quit"}));

    private ScrabbleGameView view = new ScrabbleGameView();

    /**
     * Constructor for the ScrabbleGame class.
     */
    public ScrabbleGame() {
        players = new ArrayList<>();
        scrabbleBoard = new ScrabbleBoard();
        tileBag = new TileBag();
        plays = new ArrayList<>();
        playerTurn = null;
        // The URL class throws the MalformedURLException so the program is required to handle it.
        try {
            /* URL string*/
            String validWordsURLString = "https://www.mit.edu/~ecprice/wordlist.10000";
            validWordsURL = new URL(validWordsURLString);
            validWords = getValidWordsFromUrl();
        } catch (Exception e) {
            validWords = getValidWordsFromFile();
        }
        validWords = getValidWordsFromFile();
        playerInputWord = "";
        playerInputBoardPosition = "";
        playerInputPlayDirection = Direction.HORIZONTAL;
        currentPlay = null;
        isCommand = false;
        command = "board";
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
         *          - There is not enough space in the direction player intends to play
         *          - Player does not have the required tiles
         *          - One or more of the words formed by the play is not a valid word
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
        boolean showAllPlayerTiles = false; // To be added to the XML 'configuration' file
        setupPlayers();
        playerTurn = players.getFirst();
        scrabbleBoard.drawBoard();
        playerOrder = determinePlayerTurns();

        while (true) {
            printPlayerTiles(showAllPlayerTiles);
            System.out.printf("\nTiles left in bag: %d\n", tileBag.getNumTilesLeft());
            System.out.println("Current player: " + playerTurn.getPlayerName());
            System.out.println("Current player score: " + playerTurn.getScore());
            view.setModel(this);  // assuming `this` is the ScrabbleGame instance
            view.initializeBoard();

            getInputPlay();
            if (parseInputPlay()) {
                if (isValidWord(playerInputWord)) {
                    System.out.printf("Word: %s | Position: %s | Direction: %s%n", playerInputWord, playerInputBoardPosition, playerInputPlayDirection.equals(Direction.HORIZONTAL)? "Horizontal":"Vertical");
                    if (playWord()) {
                        playerTurn.setScore(playerTurn.getScore() + tileBag.getInputScore(playerInputWord, rowNum_program, colNum_program, playerInputPlayDirection));
                        playerTurn.getMoreTiles(tileBag);
                        scrabbleBoard.drawBoard();
                        view.updateBoard();
                        changeTurns();
                    } else System.out.println("Couldn't play word.");
                } else {
                    scrabbleBoard.drawBoard();
                    System.out.println("Valid word it was not."); // Blud is NOT yoda
                }
            } else {
                if (isCommand)
                    doCommand(command);
                else System.out.println("Not a valid entry. The format of a play is\n\t[word] [coordinate] [direction]\n\te.g. school 88 v.");
            }
        }
    }

    /**
     * Sets up the players for the game.
     */
    public void setupPlayers() {
        // Retrieve player names from the option pane
        List<String> playerNames = view.setupPlayersOptionPane();

        // Check if the list is empty (in case of invalid input)
        if (playerNames.isEmpty()) {
            System.out.println("Player setup was canceled or invalid.");
            return;
        }

        // Create Player objects and set their initial scores
        for (String name : playerNames) {
            Player player = new Player(name);
            player.setScore(0); // Set initial score
            players.add(player); // Add player to the list
        }

        // Everything below this line is the text version for the setup
        if (TEXT_VERSION) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("How many players will play (Lowest 2, Highest 4): ");
            int numberOfPlayers = scanner.nextInt();

            // Error checking
            while (numberOfPlayers < 2 || numberOfPlayers > 4) {
                System.out.println("Please put in a valid number");
                System.out.print("How many players will play (Lowest 2, Highest 4): ");
                numberOfPlayers = scanner.nextInt();
            }

            // Get player names
            String name;
            for (int i = 0; i < numberOfPlayers; i++) {
                System.out.print("Player " + (i + 1) + ": What is your name: ");
                scanner = new Scanner(System.in);
                name = scanner.nextLine();
                players.add(new Player(name));
            }

            // Set the score of the players initially to 0
            for (Player player : players) {
                player.setScore(0);
            }
        }
    }

    /**
     * Changes over to the appropriate player's turn.
     */
    public void changeTurns() {
        // Increment the current player index
        currentPlayerIndex = (currentPlayerIndex + 1) % playerOrder.size();

        // Update playerTurn to the next player
        playerTurn = playerOrder.get(currentPlayerIndex);
    }

    /**
     * Plays the player's word on the game board if it is valid. Adds the word to play history if the word itself and all words formed by it are valid.
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
        String debugTag;
        debugTag = (DEBUG)? "ScrabbleGame->playWord: ":"";

        Direction dir = playerInputPlayDirection;
        int rowNum = Integer.parseInt(valueOf(playerInputBoardPosition.charAt(0)), 16);
        int colNum = Integer.parseInt(valueOf(playerInputBoardPosition.charAt(1)), 16);
        rowNum_program = rowNum - 1; // Convert to program usable coordinates. Array indexes start at 0.
        colNum_program = colNum - 1;
        if (!((14 - (dir.equals(Direction.HORIZONTAL)? colNum_program:rowNum_program) + 1) >= playerInputWord.length())) {
            System.out.printf("\n%sWord is too long. row: %d col: %d word length: %d\n",debugTag ,rowNum, colNum, playerInputWord.length());
            return false;
        }

        // The letters on the board in the area of play are in the word played
        ArrayList<Tile> lettersOnTheBoard = new ArrayList<>();
        int row;
        int col;
        Tile tile;
        for (int i = 0; i < playerInputWord.length(); i++) {
            row = dir.equals(Direction.HORIZONTAL)? rowNum_program:rowNum_program + i;
            col = dir.equals(Direction.VERTICAL)? colNum_program:colNum_program + i;
            tile = scrabbleBoard.getTileOnSquare(row, col);
            lettersOnTheBoard.add(tile); // Tile can be an actual tile or null
        }
        if (DEBUG) System.out.printf("\n%sLetters on the board in the area of play: ", debugTag);
        for (Tile _letter: lettersOnTheBoard) {
            if (_letter == null) {
                if (DEBUG) System.out.print("-");
            } else if (DEBUG) System.out.print(_letter.getLetter());
        }

        for (Tile letter:lettersOnTheBoard) {
            if (letter != null) {
                if (!(playerInputWord.contains(letter.getLetter().toLowerCase()))) {
                    if (DEBUG) System.out.printf("\n%s %s",debugTag, playerInputWord);
                    System.out.printf("\n%sLetters on the board are not in the word played\n", debugTag);
                    return false;
                }
            }
        }

        // The letters on the board in the area of play are already in the desired position
        String playerTiles = playerTurn.getTilesString();
        String dupPlayerInputWord = "".concat(playerInputWord);
        ArrayList<Tile> dupLettersOnTheBoard = (ArrayList<Tile>) lettersOnTheBoard.clone();
        String _lowCaseLetter;
        int letterIndex;
        String dupLetter;
        String letterOnBoardLetter;
        for (Tile letter:lettersOnTheBoard) {
            if (letter != null) {
                _lowCaseLetter = letter.getLetter().toLowerCase();
                letterIndex = dupLettersOnTheBoard.indexOf(letter);
                dupLetter = valueOf(dupPlayerInputWord.charAt(letterIndex)).toUpperCase();
                if (DEBUG) System.out.printf("\n%sdupLetter is %s\n", debugTag, dupLetter);
                letterOnBoardLetter = dupLettersOnTheBoard.get(letterIndex).getLetter();
                if (DEBUG) System.out.printf("\n%sletterOnBoardLetter is %s\n", debugTag, letterOnBoardLetter);

                if (!dupLetter.equals(letterOnBoardLetter)) {
                    System.out.printf("\n%sLetters on the board are not in the correct position for the word played\n", debugTag);
                    return false;
                }
                dupPlayerInputWord = dupPlayerInputWord.replaceFirst(_lowCaseLetter, "-");
                dupLettersOnTheBoard.add(letterIndex, null);
                dupLettersOnTheBoard.remove(letterIndex + 1);
            }
        }

        // Does the player making the play have the needed tiles to complete the word given the
        // letters already on the board?
        char inputChar;
        if (DEBUG) System.out.printf("\n%sPlayer's Tiles: " + playerTiles, debugTag);
        if (DEBUG) System.out.printf("\n%sWord played: " + dupPlayerInputWord, debugTag);
        if (DEBUG) System.out.printf("\n%sLetters the player has: ", debugTag);
        for (int i = 0; i < dupPlayerInputWord.length(); i++) {
            inputChar = dupPlayerInputWord.charAt(i);
            if (inputChar != '-') {
                if (DEBUG) System.out.print(inputChar);
                String _letter = valueOf(inputChar).toUpperCase();
                if (playerTiles.contains(_letter)) {
                    playerTiles = playerTiles.replaceFirst(_letter, "-");
                } else {
                    System.out.printf("\n%sPlayer does not have the needed tiles\n", debugTag);
                    return false;
                }
            }
        }
        if (DEBUG) System.out.print("\n");

        // All words formed as a result of the played word are valid
        // Create list of plays
        // Iterate over list and check all words are valid
        // Check if the play already exists and remove it from the list of plays if it does.
        putPlayOnBoard(dir, rowNum, colNum);

        // First play goes through the center of the board.
        if (!plays.contains(currentPlay)) {
            if (firstPlayIsThroughCenter()) plays.add(currentPlay);
            else {
                removePlayFromBoard(currentPlay);
                return false;
            }
        } else {
            System.out.printf("%s'%s' has already been played", debugTag, currentPlay.getWordString());
            return false;
        }

        // All words formed are valid
        if (DEBUG) printPlays();
        if (!allPlayWordsValid(dir, rowNum, colNum, "".concat(playerInputWord))) {
            plays.remove(currentPlay);
            removePlayFromBoard(currentPlay);
            if (DEBUG) printPlays();
            return false;
        }
        if (DEBUG) printPlays();
        return true;
    }

    public boolean firstPlayIsThroughCenter() {
        if (plays.isEmpty()) {
            for (Square square : currentPlay.getWordSquares()) {
                if (square.getType() == Square.Types.CENTER) {
                    return true;
                }
            }
        } else return true;
        System.out.println("First play must go through the center.");
        return false;
    }

    /**
     * Checks if all words formed by the play are valid. Adds all the unique (do not exist in play history) words formed to play history (instance variable "plays") if all words formed are valid.
     * @param dir The direction of the play.
     * @param rowNum The row number coordinate of the play
     * @param colNum The column number coordinate of the play
     * @param word The word played.
     * @return True if all the words are valid. False otherwise
     */
    public boolean allPlayWordsValid(Direction dir, int rowNum, int colNum, String word) {
        String debugTag;
        debugTag = (DEBUG)? "ScrabbleGame->allPlayWordsValid:":"";

        rowNum -= 1;
        colNum -= 1; // Convert numbers from board coordinates to program usable coordinates. Indexes of arrays start at 0.
        int row2, col2;
        ArrayList<Square> _tileWord;
        ArrayList<Square> _contributedLetters;
        ArrayList<Play> _plays = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            _tileWord = new ArrayList<>();
            _contributedLetters = new ArrayList<>();
            row2 = dir.equals(Direction.VERTICAL)? rowNum + i:rowNum;
            col2 = dir.equals(Direction.VERTICAL)? colNum: colNum + i;

            if (dir.equals(Direction.VERTICAL)) {
                if (DEBUG) System.out.println("Got in here 0.");
                while ((scrabbleBoard.getSquare(row2, col2 - 1) != null) &&
                        (scrabbleBoard.getSquare(row2, col2 - 1).getPlacedTile() != null)) {
                    if (DEBUG) System.out.printf("\n%s Vertical move 1", debugTag);
                    col2--;
                }
                while ((scrabbleBoard.getSquare(row2, col2) != null) &&
                        (scrabbleBoard.getSquare(row2, col2).getPlacedTile() != null)) {
                    if (DEBUG) System.out.printf("\n%s Vertical move 2", debugTag);
                    _tileWord.add(scrabbleBoard.getSquare(row2, col2));
                    if (col2 == colNum) {
                        _contributedLetters.add(scrabbleBoard.getSquare(row2, col2));
                    } else _contributedLetters.add(null);
                    col2++;
                }
                _plays.add(new Play(new Player("nil"), _tileWord, _contributedLetters));
            } else if (dir.equals(Direction.HORIZONTAL)) {
                if (DEBUG) System.out.println("Got in here 1.");
                while ((scrabbleBoard.getSquare(row2 - 1, col2) != null) &&
                        (scrabbleBoard.getSquare(row2 - 1, col2).getPlacedTile() != null)) {
                    if (DEBUG) System.out.printf("\n%s Horizontal move 1", debugTag);
                    row2--;
                }
                while ((scrabbleBoard.getSquare(row2, col2) != null) &&
                        (scrabbleBoard.getSquare(row2, col2).getPlacedTile() != null)) {
                    if (DEBUG) System.out.printf("\n%s Horizontal move 2", debugTag);
                    _tileWord.add(scrabbleBoard.getSquare(row2, col2));
                    if (row2 == rowNum) {
                        _contributedLetters.add(scrabbleBoard.getSquare(row2, col2));
                    } else _contributedLetters.add(null);
                    row2++;
                }
                _plays.add(new Play(playerTurn, _tileWord, _contributedLetters)); // Placeholder players that are not relevant to the game.
            }
        }

        // Check that all words formed are valid
        for (Play _pl: _plays) {
            if (DEBUG) System.out.printf("%s word is %s%n", debugTag, _pl.getWordString());
            if (!isValidWord(_pl.getWordString())) {
                System.out.printf("%s'%s' is not a word.\n", debugTag, _pl.getWordString());
                return false;
            }
        }

//        // Check that all words are unique
//        plays.removeLast();
//        for (Play _pl: _plays) {
//            if (plays.contains(_pl)) {
//                System.out.printf("\n%s'%s' has already been played.\n", debugTag, _pl.getWordString());
//                removePlayFromBoard(currentPlay);
//                return false;
//            }
//        }
//        plays.add(currentPlay);

        _plays.removeIf(_pl -> (/*this.plays.contains(_pl) ||*/ _pl.getWordString().length() == 1));
        boolean connects = false;
        connects = currentPlay.getContributedLettersString().contains("-"); // The word is made by contributing a few letters from the players tiles to what is already on the board.

        for (Play _pl: _plays) {
            if (DEBUG) System.out.printf("%s word left is %s%n", debugTag, _pl.getWordString());
            if (_pl.getContributedLettersString().contains("-")) { // At least one of the words created as a result of the main play are created by adding letters to what is already on the board.
                connects = true;
            }
        }

        if (plays.size() > 1) {
            if (connects) plays.addAll(_plays);
            else {
                System.out.println("Word must connect to at least one of the letters on the board.");
                return false;
            }
        }

        return true;
    }

    /**
     * Puts a play on the board.
     * @param dir The direction of the play.
     * @param rowNum The row number coordinate of the play.
     * @param colNum The column number coordinate of the play.
     */
    public void putPlayOnBoard(Direction dir, int rowNum, int colNum) {
        ArrayList<Square> _word = new ArrayList<>();
        ArrayList<Square> _contributedLetters = new ArrayList<>();
        rowNum -= 1;
        colNum -= 1; // Convert numbers from board coordinates to program usable coordinates. Indexes of arrays start at 0.
        // Making the play
        // Taking the necessary letters from the player's tile deck when needed
        Tile playTile;
        int row, col;
        for (int i = 0; i < playerInputWord.length(); i++) {
            row = dir.equals(Direction.HORIZONTAL)? rowNum:rowNum + i;
            col = dir.equals(Direction.VERTICAL)? colNum:colNum + i;
            ArrayList<Tile> playerTurnTiles = playerTurn.getTiles();
            if (scrabbleBoard.getSquare(row, col).getPlacedTile() == null) {
                playTile = playerTurn.takeTile(valueOf(playerInputWord.charAt(i)).toUpperCase());
                scrabbleBoard.placeTile(row, col, playTile);
                _word.add(scrabbleBoard.getSquare(row, col));
                _contributedLetters.add(scrabbleBoard.getSquare(row, col));
            } else{
                _word.add(scrabbleBoard.getSquare(row, col));
                _contributedLetters.add(null);
            }
        }
        currentPlay= new Play(playerTurn, _word, _contributedLetters);
    }

    /**
     * Removes a play from the board
     * @param play The play to be removed from the board
     */
    public void removePlayFromBoard(Play play) {
        Tile tile;
        for (Square square: play.getContributedLetters()) {
            if (square == null) {
                continue;
            }
            tile = square.getPlacedTile();
            if (tile.getPlayer().equals(play.getPlayer())) {
                playerTurn.returnTile(square.removePlacedTile());
                if (DEBUG) System.out.printf("ScrabbleGame->removePlayFromBoard: Letter removed: %s, Player was %s", tile.getLetter(), tile.getPlayer().getPlayerName());
            } else if (DEBUG) System.out.printf("ScrabbleGame->removePlayFromBoard: Letter not removed: %s, Player was %s", tile.getLetter(), tile.getPlayer().getPlayerName());
            if (DEBUG) System.out.println("-");
        }
    }

    /**
     * Gets the order of play for the players.
     * @return The order of play for the players.
     */
    public ArrayList<Player> determinePlayerTurns() {
        ArrayList<Player> returnTurns = new ArrayList<>();
        ArrayList<String> playerDraws = new ArrayList<>();
        ArrayList<String> sortedPlayerDraws = new ArrayList<>();
        ArrayList<Tile> tile;

        // Initialize each player's tiles
        for (Player player : players) {
            tile = tileBag.getRandomTilesFromBag(1);
            player.setPlayerTiles(tile);
            playerDraws.add(tile.getFirst().getLetter()); // Use getFirst() to access the first tile
            player.getMoreTiles(tileBag); // Take the number of necessary tiles needed.
        }

        int lowestIndex = 0;
        String minChar;

        // Determine player turns based on drawn tiles
        for (Player player : players) {
            minChar = playerDraws.getFirst(); // Use getFirst() to access the first element
            lowestIndex = 0;

            for (int i = 0; i < playerDraws.size(); i++) {
                if (minChar.charAt(0) > playerDraws.get(i).charAt(0)) {
                    minChar = playerDraws.get(i);
                    lowestIndex = i;
                }
            }

            sortedPlayerDraws.add(minChar);
            playerDraws.remove(lowestIndex); // Remove the drawn tile for that player
            playerDraws.add(lowestIndex, "a"); // Mark as used
            returnTurns.add(players.get(lowestIndex)); // Add player to return list
        }

        // Debug output for player turns
        for (int j = 0; j < returnTurns.size(); j++) {
            System.out.printf("\nPlayer: %s Tile Drawn: %s", returnTurns.get(j).getPlayerName(), sortedPlayerDraws.get(j));
        }

        // Print player order
        System.out.print("\nPlayer order is ");
        for (Player player : returnTurns) {
            System.out.printf(player.getPlayerName() + " --> ");
        }
        System.out.print("Repeat\n\n");

        // Set the first player in the returnTurns as playerTurn
        if (!returnTurns.isEmpty()) {
            playerTurn = returnTurns.getFirst();
        }

        return returnTurns;
    }

    /**
     * Retrieves a set of valid words from the url given in validWordsURL.
     * This function was mostly to practice the HttpURLConnection, BufferedReader, and Scanner classes.
     * @return A HashSet of the valid words from the validWordsURL
     */
    public HashSet<String> getValidWordsFromUrl() {
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
    public HashSet<String> getValidWordsFromFile() {
        HashSet<String> returnSet = new HashSet<>();
        // Holds the file path for the text file containing the valid words*/
        String validWordsFile = "resources/valid_words.txt";
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
    public void getInputPlay() {
        Scanner inputWord = new Scanner(System.in);
        String inputPrompt = "Word to play >> ";
        System.out.print(inputPrompt);
        playerInput = inputWord.nextLine();
    }

    /**
     * Sets the player's input to the provided string.
     * @param input the word to set as the player's input.
     */
    public void setInputPlay(String input) {
        this.playerInput = input;
    }


    /**
     * Parses through the user's input and checks for errors. It updates the playerInputWord, playerInputBoardPosition, and playerInputPlayDirection fields.
     * @return Returns true if both word and board position parameters are valid. Returns false if board position is out of range.
     */
    public boolean parseInputPlay() {
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
        } else if (commands.contains(_tempWord)) {
            isCommand = true;
            command = _tempWord;
            return false;
        } else return false;

        if (tokenizer.hasNext()) { // Direction of the play
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

    public void doCommand(String command) {
        if (command.equals("debug")) {
            DEBUG = true;
        } else if (command.equals("noDebug")) {
            DEBUG = false;
        } else if (command.equals("board")) {
            scrabbleBoard.drawBoard();
        }
        isCommand = false;
    }

    /**
     * Prints all player tiles in a visually appealing format.
     * @param allPlayers Decides if the tiles of all players should be shown. If true, all player tiles are shown, if false, only the current player tiles are shown.
     */
    public void printPlayerTiles(boolean allPlayers) {
        ArrayList<Player> _currentPlayer = new ArrayList<>();
        _currentPlayer.add(playerTurn); // ArrayList 'wrapper' that only contains the current player. It is for the sake of the following for loop and to avoid code duplication.
        for (Player player : (allPlayers)? players:_currentPlayer) {
            // Get the player's tiles
            ArrayList<Tile> playerTiles = player.getTiles();

            // Print each tile
            System.out.printf("\n%s's tiles: \n", player.getPlayerName());
            System.out.println("-" + "------".repeat(player.getTiles().size()));
            System.out.print("|");
            for (Tile tile : playerTiles) {
                System.out.print(" " +
                        player.getPlayerColour() +
                        "(" + tile.getLetter()  + ")"
                        + ScrabbleBoard.RESET +
                        " |"); // Assuming getLetter() returns a char or String
            }
            System.out.println(); // Move to the next line after printing all tiles of the current player
            System.out.println("-" + "------".repeat(player.getTiles().size()));
        }
        System.out.println();
    }

    /**
     * Determines if a word is in the list of valid words.
     * @param word The word to check
     * @return true if the word is valid. false otherwise.
     */
    public boolean isValidWord(String word) {
        if (word == null) {return false;}
        return validWords.contains(word.toLowerCase());
    }

    /**
     * Prints the plays in play history. Mostly used for debug purposes.
     */
    public void printPlays() {
        if (DEBUG) System.out.println("Current plays in history:");
        for (Play _p: plays) {
            if (DEBUG) System.out.printf("%s", _p.getWordString());
            if (DEBUG) System.out.printf("   %s\n", _p.getContributedLettersString());
        }
        if (DEBUG) System.out.println("Current plays in history END");
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public ScrabbleBoard getScrabbleBoard() {
        return scrabbleBoard;
    }

    public ArrayList<Play> getPlays() {
        return plays;
    }

    public String getPlayerInput() {
        return playerInput;
    }

    public String getPlayerInputWord() {
        return playerInputWord;
    }

    public String getPlayerInputBoardPosition() {
        return playerInputBoardPosition;
    }

    public Direction getPlayerInputPlayDirection() {
        return playerInputPlayDirection;
    }

    public ArrayList<Player> getPlayerOrder() {
        return playerOrder;
    }

    public Play getCurrentPlay() {
        return currentPlay;
    }

    public String getCommand() {
        return command;
    }

    public boolean isCommand() {
        return isCommand;
    }

    public static void setDEBUG(boolean DEBUG) {
        ScrabbleGame.DEBUG = DEBUG;
    }

    public void setPlayerInput(String playerInput) {
        this.playerInput = playerInput;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setTileBag(TileBag tileBag) {
        this.tileBag = tileBag;
    }

    public void setPlays(ArrayList<Play> plays) {
        this.plays = plays;
    }

    public void setPlayerInputWord(String playerInputWord) {
        this.playerInputWord = playerInputWord;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setPlayerInputBoardPosition(String playerInputBoardPosition) {
        this.playerInputBoardPosition = playerInputBoardPosition;
    }

    public void setPlayerInputPlayDirection(Direction playerInputPlayDirection) {
        this.playerInputPlayDirection = playerInputPlayDirection;
    }

    public void setPlayerOrder(ArrayList<Player> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public void setCurrentPlay(Play currentPlay) {
        this.currentPlay = currentPlay;
    }

    public void setCommand(boolean command) {
        isCommand = command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getNumTilesLeft() { return tileBag.getNumTilesLeft(); }

    public String getCurrentPlayerName() { return playerTurn.getPlayerName(); }

    public int getCurrentPlayerScore() { return playerTurn.getScore(); }

    public TileBag getTileBag() {
        return tileBag;
    }

    public static void main(String[] args) {
        ScrabbleGame game = new ScrabbleGame();
        game.game();
    }

}
