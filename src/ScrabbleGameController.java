import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The ScrabbleGame class simulates as at this version a turn by turn text version of the
 * game Scrabble.
 * @author Abdul Babatunde
 * @version November 12, 2024
 */

public class ScrabbleGameController implements ActionListener {
    private ScrabbleGame model;
    private ScrabbleGameView view;
    private Tile selectedTile;  // To store the selected tile from the player's rack
    private Tile selectedBoardTile;
    private Stack<TileState> history;  // Stack to store the previous state of board tiles
    private List<Tile> playedTiles;  // List to store the tiles played on the board
    private int selectedTileIndex = -1;  // Initialize with -1 to indicate no tile selected
    private int firstRow = -1, firstCol = -1;  // Coordinates of the first letter placed
    private int lastRow = -1, lastCol = -1;    // Coordinates of the last letter placed
    private int clickCounter = 0;
    private boolean isLocked = false;
    private boolean isRepeated = false;


    public ScrabbleGameController(ScrabbleGame model, ScrabbleGameView view) {
        this.model = model;
        this.view = view;
        this.history = new Stack<>();  // Initialize the history stack
        this.playedTiles = new ArrayList<>();  // Initialize the list of played tiles
        model.setController(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String command = source.getActionCommand();
        //System.out.println("Button clicked with action command: " + command); // Debugging

        // Handle tile button clicks (player's rack)
        if (command.startsWith("Tile")) {
            // Extract tile index (1-based to 0-based)
            selectedTileIndex = Integer.parseInt(command.split(" ")[1]) - 1;
            selectedTile = model.getPlayerTurn().getTiles().get(selectedTileIndex);  // Get selected tile from player's rack
            // System.out.println("Selected Tile: " + selectedTile.getLetter() + " with value: " + selectedTile.getValue());
        }

        // Handle board tile clicks
        else if (command.startsWith("BoardTile")) {
            int rowIndex = Integer.parseInt(command.split(" ")[1]);
            int colIndex = Integer.parseInt(command.split(" ")[2]);

            savePreviousState(rowIndex, colIndex);

            if (selectedTile != null) {
                view.editSquare(rowIndex, colIndex, selectedTile.getLetter(), selectedTile.getValue());
//                 System.out.println("Board updated at (" + rowIndex + ", " + colIndex + ") with tile: " + selectedTile.getLetter());

                // Track the first placement
                if (firstRow == -1 && firstCol == -1) {
                    firstRow = rowIndex;
                    firstCol = colIndex;
                    // System.out.println("First letter placed at: (" + firstRow + ", " + firstCol + ")");
                }

                // Update the last placement
                lastRow = rowIndex;
                lastCol = colIndex;
                // System.out.println("Last letter placed at: (" + lastRow + ", " + lastCol + ")");

                // Add the selected tile to the played tiles list
                playedTiles.add(selectedTile);
                System.out.println(generatePlacementString());
                // System.out.println("Played tiles: " + formatPlayedTiles());
                // System.out.println("Word was placed " + determineWordDirection());


                if (selectedTileIndex != -1) {
                    // Disable the tile button at the correct index
                    JButton tileButton = view.getPlayerRackButton(selectedTileIndex);
                    tileButton.setEnabled(false);
                    // System.out.println("Disabled the tile button at index: " + selectedTileIndex);
                    selectedTileIndex = -1;  // Reset the selectedTileIndex after use
                }

                selectedTile = null;  // Clear the selected tile after placing it on the board
                view.disableBoardTile(rowIndex,colIndex);
            }
        }

        else if (command.startsWith("TwoClickBoardTile")) {
            int rowIndex = Integer.parseInt(command.split(" ")[1]);
            int colIndex = Integer.parseInt(command.split(" ")[2]);

            // Create a new tile from the board based on the row and column
            selectedBoardTile = createTileFromBoard(rowIndex, colIndex);
            lastRow = rowIndex;
            lastCol = colIndex;
            // Check if the lock is active
            if (!isLocked) {
                // Increment the counter
                clickCounter++;
                // Only execute the following code if the counter is odd
                if (clickCounter % 2 != 0) {
                    // Lock further increments for 2 seconds
                    isLocked = true;
                    // Add the selected tile to playedTiles
                    playedTiles.add(selectedBoardTile);
                    System.out.println("Added tile to playedTiles: " + selectedBoardTile.getLetter());
                    System.out.println(generatePlacementString());
                    // Show confirmation message
                    JOptionPane.showMessageDialog(null, "Added letter to your played tiles");
                    // Track the first placement
                    if (firstRow == -1 && firstCol == -1) {
                        firstRow = rowIndex;
                        firstCol = colIndex;
                        // System.out.println("First letter placed at: (" + firstRow + ", " + firstCol + ")");
                    }

                    // Use a Timer to unlock after 2 seconds
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            isLocked = false;
                        }
                    });
                    timer.setRepeats(false); // Ensure the timer only runs once
                    timer.start();
                }
            }

            // Reset the selectedBoardTile after use
            selectedBoardTile = null;
        }


        else if (command.equals("Pass")) {
            while (!history.isEmpty()) {
                undoLastMove();  // Undo each move in the history stack
            }
            playedTiles.clear();  // Clear the played tiles list
            firstRow = -1;
            firstCol = -1;
            lastRow = -1;
            lastCol = -1;
            model.changeTurns();

            // Update view: refresh board and scores
            view.updateBoard();
            view.updateLabels();
        }

        // Handle "Clear" button action (Undo action)
        else if (command.equals("Clear")) {
            while (!history.isEmpty()) {
                undoLastMove();  // Undo each move in the history stack
            }
            playedTiles.clear();  // Clear the played tiles list
            firstRow = -1;
            firstCol = -1;
            lastRow = -1;
            lastCol = -1;
            // System.out.println("Played tiles after clear: " + formatPlayedTiles());
            // System.out.println("Cleared all moves.");
        }

        // Handle "Play" button action (clear the history)
        else if (command.equals("Play")) {
            if (!playedTiles.isEmpty()) {
                // Generate the placement string based on the GUI's state
                String placementString = generatePlacementString();
                //placementString = getFullWord(placementString);
                System.out.println(placementString);
                model.setInputPlay(placementString);

                // Call model's parsing and validation
                if (model.parseInputPlay()) {
                    if (model.isValidWord(model.getPlayerInputWord())) {
                        if (model.playWord()) {
                            // Update model: recalculate score and get new tiles
                            model.getPlayerTurn().setScore(
                                    model.getPlayerTurn().getScore() +
                                            model.getTileBag().getInputScore(
                                                    model.getPlayerInputWord(),
                                                    model.getRowNumProgram(),
                                                    model.getColNumProgram(),
                                                    model.getPlayerInputPlayDirection()
                                            )
                            );
                            model.getPlayerTurn().getMoreTiles(model.getTileBag());
                            model.getScrabbleBoard().drawBoard();

                            // Clear controller's state
                            clearHistory();
                            playedTiles.clear();
                            firstRow = -1;
                            firstCol = -1;
                            lastRow = -1;
                            lastCol = -1;

                            // Re-enable all tile buttons
                            view.enableAllTileButtons();

                            // Change turns
                            model.changeTurns();

                            // Update view: refresh board and scores
                            view.updateBoard();
                            view.updateLabels();
                        } else {
                            JOptionPane.showMessageDialog(null, "Words must connect or start from center.");
                            view.updateBoard();
                            view.updateLabels();
                            //clearHistory();
                            undoLastMove();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, model.getPlayerInputWord() + " is an invalid word.");
                        view.updateBoard();
                        view.updateLabels();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Spell out your word correctly by clicking the button");
                    view.updateBoard();
                    view.updateLabels();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No tiles were played.");
                view.updateBoard();
                view.updateLabels();
            }
        }
    }

    private void enableAITilesForClick(List<Tile> aiPlayedTiles, List<int[]> aiPlayedCoordinates) {
        // Iterate through the tiles played by the AI
        for (int i = 0; i < aiPlayedTiles.size(); i++) {
            int row = aiPlayedCoordinates.get(i)[0];
            int col = aiPlayedCoordinates.get(i)[1];

            // Get the button representing this tile on the board
            JButton boardTileButton = view.getBoardSquare(row, col);

            // Set the action command to allow "TwoClickBoardTile"
            boardTileButton.setActionCommand("TwoClickBoardTile " + row + " " + col);

            // Enable the button to allow interaction by the other player
            boardTileButton.setEnabled(true);

            view.setBoardSquare(boardTileButton, row, col);
            // Optionally, update the button visually if needed (e.g., color or label)
            //boardTileButton.setText(aiPlayedTiles.get(i).getLetter());
        }
    }


    // Calculate the word value
    private int calculateWordScore(String word) {
        int score = 0;

        String[] letters = word.split("");  // This splits the word into individual characters

        // Iterate over the array of string characters
        for (String letter : letters) {
            score += model.getTileBag().getTileValue(letter);
        }

        return score;
    }

    // Function to get the full word by checking tile above, below, to the left/right
    private String getFullWord(String aiWord) {
        ScrabbleBoard gameBoard = model.getScrabbleBoard();
        Square[][] board = gameBoard.getBoard();

        // Check if the board is completely empty
        boolean isBoardEmpty = true;
        for (int row = 0; row < gameBoard.SIZE; row++) {
            for (int col = 0; col < gameBoard.SIZE; col++) {
                if (board[row][col].getPlacedTile() != null) {
                    isBoardEmpty = false;
                    break;
                }
            }
            if (!isBoardEmpty) break;
        }

        // Handle the empty board case
        if (isBoardEmpty) {
            boolean placeVertically = Math.random() < 0.5;
            String direction = placeVertically ? "v" : "h";
            return aiWord + " 88 " + direction;
        }

        String[] letters = aiWord.split("");
        int wordLength = aiWord.length();

        // Loop through the board to find the first occurrence of any letter in aiWord
        for (int row = 0; row < gameBoard.SIZE; row++) {
            for (int col = 0; col < gameBoard.SIZE; col++) {
                if (board[row][col].getPlacedTile() != null) {
                    String placedLetter = board[row][col].getPlacedTile().getLetter();

                    // Check if the placed letter matches any letter in aiWord
                    for (int i = 0; i < wordLength; i++) {
                        if (placedLetter.equals(letters[i])) {
                            // Ensure that there is enough space for placement in all 4 directions
                            if (!prePlacementValidation(aiWord, board, row, col, i)) {
                                continue;  // Skip if the word cannot be placed
                            }

                            // Try placing the word vertically (up-down)
                            if (canPlaceWord(aiWord, board, row, col, i, "v")) {
                                String placementString = generateWordString(aiWord,
                                        Integer.toHexString(row - i + 1).toUpperCase(),
                                        Integer.toHexString(col + 1).toUpperCase(),
                                        "v");

                                // After placement, validate if the surrounding words are valid
                                if (postPlacementValidation(aiWord, board, row, col, i, "v")) {
                                    return placementString;  // Valid placement found
                                }
                            }

                            // Try placing the word horizontally (left-right)
                            if (canPlaceWord(aiWord, board, row, col, i, "h")) {
                                String placementString = generateWordString(aiWord,
                                        Integer.toHexString(row + 1).toUpperCase(),
                                        Integer.toHexString(col - i + 1).toUpperCase(),
                                        "h");

                                // After placement, validate if the surrounding words are valid
                                if (postPlacementValidation(aiWord, board, row, col, i, "h")) {
                                    return placementString;  // Valid placement found
                                }
                            }
                        }
                    }
                }
            }
        }

        // No valid placement found
        System.out.println("No valid placement found for word: " + aiWord);
        return "No valid placement found";
    }

    private boolean prePlacementValidation(String aiWord, Square[][] board, int row, int col, int letterIndex) {
        // Ensure that there is enough space in all four directions before placing the word
        if (!canPlaceWordWithBuffer(aiWord, board, row, col, letterIndex, "v") &&
                !canPlaceWordWithBuffer(aiWord, board, row, col, letterIndex, "h")) {
            return false;  // Cannot place word
        }
        return true;
    }

    private boolean canPlaceWordWithBuffer(String aiWord, Square[][] board, int row, int col, int letterIndex, String direction) {
        // Check if there's space for the word and one extra tile in the direction
        if (direction.equals("v")) {
            if (row - 1 >= 0 && board[row - 1][col].getPlacedTile() != null) return false;
            if (row + aiWord.length() < board.length && board[row + aiWord.length()][col].getPlacedTile() != null) return false;
        } else { // horizontal
            if (col - 1 >= 0 && board[row][col - 1].getPlacedTile() != null) return false;
            if (col + aiWord.length() < board[row].length && board[row][col + aiWord.length()].getPlacedTile() != null) return false;
        }
        return true;
    }

    private boolean postPlacementValidation(String aiWord, Square[][] board, int row, int col, int letterIndex, String direction) {
        // After placing the word, check the surrounding directions (up, down, left, right)
        String combinedWord;

        // Check vertical (up/down)
        if (direction.equals("v")) {
            if (!checkDirection(aiWord, board, row, col, "v", letterIndex)) {
                return false;  // Invalid combination found
            }
        } else { // Horizontal (left/right)
            if (!checkDirection(aiWord, board, row, col, "h", letterIndex)) {
                return false;  // Invalid combination found
            }
        }

        return true;  // If all checks are valid
    }

    private boolean checkDirection(String aiWord, Square[][] board, int row, int col, String direction, int letterIndex) {
        StringBuilder combinedWord = new StringBuilder();

        if (direction.equals("v")) { // Vertical direction
            int upRow = row;
            int downRow = row + letterIndex;

            // Check upwards direction for combined word
            while (upRow >= 0 && board[upRow][col].getPlacedTile() != null) {
                combinedWord.insert(0, board[upRow][col].getPlacedTile().getLetter());
                upRow--;
            }

            // Check downwards direction for combined word
            while (downRow < board.length && board[downRow][col].getPlacedTile() != null) {
                combinedWord.append(board[downRow][col].getPlacedTile().getLetter());
                downRow++;
            }

            // Check the first letter (upward direction - top)
            if (row - 1 >= 0 && board[row - 1][col].getPlacedTile() != null) {
                combinedWord.insert(0, board[row - 1][col].getPlacedTile().getLetter());
            }

            // Check the last letter (downward direction - bottom)
            if (row + letterIndex < board.length && board[row + letterIndex][col].getPlacedTile() != null) {
                combinedWord.append(board[row + letterIndex][col].getPlacedTile().getLetter());
            }
        } else { // Horizontal direction
            int leftCol = col;
            int rightCol = col + letterIndex;

            // Check leftwards direction for combined word
            while (leftCol >= 0 && board[row][leftCol].getPlacedTile() != null) {
                combinedWord.insert(0, board[row][leftCol].getPlacedTile().getLetter());
                leftCol--;
            }

            // Check rightwards direction for combined word
            while (rightCol < board[row].length && board[row][rightCol].getPlacedTile() != null) {
                combinedWord.append(board[row][rightCol].getPlacedTile().getLetter());
                rightCol++;
            }

            // Check the first letter (left direction - left side)
            if (col - 1 >= 0 && board[row][col - 1].getPlacedTile() != null) {
                combinedWord.insert(0, board[row][col - 1].getPlacedTile().getLetter());
            }

            // Check the last letter (right direction - right side)
            if (col + letterIndex < board[row].length && board[row][col + letterIndex].getPlacedTile() != null) {
                combinedWord.append(board[row][col + letterIndex].getPlacedTile().getLetter());
            }
        }

        // After combining, validate if the formed word is valid
        return model.isValidWord(combinedWord.toString());
    }

    // Helper method to check if the word can be placed (both vertical and horizontal)
    private boolean canPlaceWord(String aiWord, Square[][] board, int row, int col, int offset, String direction) {
        int wordLength = aiWord.length();

        for (int i = 0; i < wordLength; i++) {
            int currentRow = direction.equals("v") ? row + i : row;
            int currentCol = direction.equals("h") ? col + i : col;

            if (currentRow < 0 || currentRow >= board.length || currentCol < 0 || currentCol >= board[0].length) {
                return false; // Out of bounds
            }

            Square currentSquare = board[currentRow][currentCol];
            if (currentSquare.getPlacedTile() != null && !aiWord.substring(i, i + 1).equals(currentSquare.getPlacedTile().getLetter())) {
                return false; // Letter mismatch
            }
        }

        return true;
    }


    // Generate the placement string
    private String generateWordString(String aiWord, String row, String col, String direction) {
        return aiWord + " " + row + col + " " + direction;
    }

    public void setIsRepeated(boolean isRepeated){
        this.isRepeated = isRepeated;
    }

    // handle AI play
    public void handleAITurn() {
        List<Tile> tiles = model.getPlayerTurn().getTiles();
        StringBuilder playWordBuilder = new StringBuilder();

        for (Tile tile : tiles) {
            playWordBuilder.append(tile.getLetter());
        }

        String playWord = playWordBuilder.toString();
        List<String> possibleWords = PowerSetWithPermutations.generateValidWords(playWord);

        String bestWord = "";
        int maxScore = 0;

        // Check each word for validity and score
        for (String word : possibleWords) {
            int score = calculateWordScore(word);
            if (score > maxScore) {
                maxScore = score;
                bestWord = word;
            }
        }

        String wordToPlace = getFullWord(bestWord);

        // Loop to keep trying words until a valid placement is found or list is empty
        while (wordToPlace.equals("No valid placement found") && !possibleWords.isEmpty()) {
            // Remove the current bestWord from possibleWords
            possibleWords.remove(bestWord);
            maxScore = 0;
            bestWord = "";

            // Recalculate the best word from the remaining possibleWords
            for (String word : possibleWords) {
                int score = calculateWordScore(word);
                if (score > maxScore) {
                    maxScore = score;
                    bestWord = word;
                }
            }

            // Try to place the new best word
            if (!bestWord.isEmpty()) {
                wordToPlace = getFullWord(bestWord);
            } else {
                // If no more words are available, exit the loop
                break;
            }
        }

        System.out.println(wordToPlace);
        model.setInputPlay(wordToPlace);

        if (wordToPlace.equals("No valid placement found")){
            if(!isRepeated){
                // If no valid word was placed, pass the turn
                JOptionPane.showMessageDialog(
                        view,
                        "AI could not find a valid move and has passed its turn.",
                        "AI Turn Complete",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }


            playedTiles.clear();  // Clear the played tiles list
            firstRow = -1;
            firstCol = -1;
            lastRow = -1;
            lastCol = -1;
            model.changeTurns();

            // Update view: refresh board and scores
            view.updateBoard();
            view.updateLabels();

            return;
        }

        // Call model's parsing and validation
        if (model.parseInputPlay()) {
            if (model.isValidWord(model.getPlayerInputWord())) {
                if (model.playWord()) {
                    // Update model: recalculate score and get new tiles
                    model.getPlayerTurn().setScore(
                            model.getPlayerTurn().getScore() +
                                    model.getTileBag().getInputScore(
                                            model.getPlayerInputWord(),
                                            model.getRowNumProgram(),
                                            model.getColNumProgram(),
                                            model.getPlayerInputPlayDirection()
                                    )
                    );

                    int row = model.getRowNumProgram();
                    int col = model.getColNumProgram();
                    String dir = model.getPlayerInputPlayDirection() == ScrabbleGame.Direction.HORIZONTAL ? "h" : "v";

                    Square[][] squares = view.getSquares();

                    String[] tileString = model.getPlayerInputWord().split("");
                    ArrayList<Tile> tile = new ArrayList<>();

                    for (String play : tileString){
                        tile.add(new Tile(play));
                    }

                    if (dir.equals("h")){
                        List<int[]> aiPlayedCoordinates = new ArrayList<>();
                        int count = 0;
                        for (Tile tileToPlay : tile){
                            if(squares[row][col].getPlacedTile() == null){
                                model.getScrabbleBoard().placeTile(row, col, tileToPlay);
                                squares[row][col].setPlacedTile(tileToPlay);
                                aiPlayedCoordinates.add(new int[] {row, col});
                            }
                            else{
                                aiPlayedCoordinates.add(new int[] {row, col});
                            }
                            col++;
                            count++;
                        }
                        enableAITilesForClick(tile, aiPlayedCoordinates);
                    }
                    else if (dir.equals("v")){
                        List<int[]> aiPlayedCoordinates = new ArrayList<>();
                        int count = 0;
                        for (Tile tileToPlay : tile){
                            if(squares[row][col].getPlacedTile() == null){
                                model.getScrabbleBoard().placeTile(row, col, tileToPlay);
                                squares[row][col].setPlacedTile(tileToPlay);
                                aiPlayedCoordinates.add(new int[] {row, col});
                            }
                            else{
                                aiPlayedCoordinates.add(new int[] {row, col});
                            }
                            row++;
                            count++;
                        }
                        enableAITilesForClick(tile, aiPlayedCoordinates);
                    }

                    view.setSquares(squares);

                    model.getPlayerTurn().getMoreTiles(model.getTileBag());
                    model.getScrabbleBoard().drawBoard();

                    // Show dialog to inform the player
                    JOptionPane.showMessageDialog(
                            view,
                            "AI has played successfully! It's now your turn.",
                            "AI Turn Complete",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Clear controller's state
                    clearHistory();
                    playedTiles.clear();
                    firstRow = -1;
                    firstCol = -1;
                    lastRow = -1;
                    lastCol = -1;

                    // Re-enable all tile buttons
                    view.enableAllTileButtons();

                    // Change turns
                    model.changeTurns();

                    // Update view: refresh board and scores
                    view.updateBoard();
                    view.updateLabels();

                    return;

                } else {
//                    JOptionPane.showMessageDialog(null, "Words must connect or start from center.");
                    view.updateBoard();
                    view.updateLabels();
                    clearHistory();
                }
            }
        }
    }



    // Method to save the previous state of a tile before it is updated
    private void savePreviousState(int rowIndex, int colIndex) {
        String letter = view.getTileLetterAt(rowIndex, colIndex);
        int value = view.getTileValueAt(rowIndex, colIndex);
        TileState previousState = new TileState(letter, value, rowIndex, colIndex, selectedTileIndex);
        history.push(previousState);  // Save the previous state to history stack
    }

    // Method to create a Tile using the letter and value at the specified board index
    private Tile createTileFromBoard(int rowIndex, int colIndex) {
        // Get the letter and value from the specified index on the board
        String letter = view.getTileLetterAt(rowIndex, colIndex);
        int value = view.getTileValueAt(rowIndex, colIndex);

        // Create a new Tile using the letter and value
        Tile newTile = new Tile(letter, value);

        // Return the newly created tile
        return newTile;
    }

    // Method to re-enable all tile buttons after the "Play" button is clicked
    private void enableAllTileButtons() {
        // Assuming `view.getPlayerRackButton(index)` provides access to the buttons in the player's rack
        for (int i = 0; i < 7; i++) {
            JButton tileButton = view.getPlayerRackButton(i);
            if (tileButton != null) {
                tileButton.setEnabled(true);  // Enable the button
            }
        }
        // System.out.println("All tile buttons have been re-enabled.");
    }



    // Method to undo the last placed tile
    private void undoLastMove() {
        if (!history.isEmpty()) {
            TileState lastState = history.pop();  // Get the last state
            // System.out.println("Undid last move at (" + lastState.getRow() + ", " + lastState.getCol() + ")");
            // System.out.println("Previously at this tile: Letter = " + lastState.getLetter() + ", Value = " + lastState.getValue());

            // Revert the board tile to its previous state
            view.editSquare(lastState.getRow(), lastState.getCol(), lastState.getLetter(), lastState.getValue());
            view.enableBoardTile(lastState.getRow(), lastState.getCol());

            // Remove the last played tile
            if (!playedTiles.isEmpty()) {
                playedTiles.remove(playedTiles.size() - 1);
            }

            // Re-enable the corresponding tile button in the player's rack
            int tileIndex = lastState.getTileIndex();
            if (tileIndex != -1) {
                JButton tileButton = view.getPlayerRackButton(tileIndex);
                tileButton.setEnabled(true);
            }

            // Update first and last placement
            if (history.isEmpty()) {
                firstRow = -1;
                firstCol = -1;
            } else {
                TileState peekState = history.peek();
                lastRow = peekState.getRow();
                lastCol = peekState.getCol();
            }
            // Reset selected tile and word direction if needed
            selectedTile = null;
            selectedTileIndex = -1;
        }
    }

    // Method to clear the history stack after the "Play" button is pressed
    private void clearHistory() {
        history.clear();
    }

    private String formatPlayedTiles() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : playedTiles) {
            sb.append(tile.getLetter());
        }
        return sb.toString();
    }

    private String determineWordDirection() {
        if (firstRow == lastRow && firstCol != lastCol) {
            return "h";
        } else if (firstCol == lastCol && firstRow != lastRow) {
            return "v";
        } else {
            return "i";  // This case should not occur if the placements are consecutive
        }
    }

    private String generatePlacementString() {
        // Format played tiles
        String playedWord = formatPlayedTiles();

        // Map numeric indices to board letters
        String rowMapping = mapIndexToBoard(firstRow);
        String colMapping = mapIndexToBoard(firstCol);

        // Determine direction
        String direction;
        if (firstRow == lastRow && firstCol != lastCol) {
            direction = "h";  // Horizontal
        } else if (firstCol == lastCol && firstRow != lastRow) {
            direction = "v";  // Vertical
        } else {
            direction = "i";  // Invalid
        }

        // Create the formatted string
        return playedWord + " " + rowMapping + colMapping + " " + direction;
    }

    private String mapIndexToBoard(int index) {
        // Board index mapping from 0-14 to 1-9, A-F
        String[] boardMapping = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        if (index >= 0 && index < boardMapping.length) {
            return boardMapping[index];
        }
        return "";  // Return an empty string for invalid indices
    }


    // Inner class to represent the state of a tile (letter, value, position)
    private class TileState {
        private String letter;
        private int value;
        private int row;
        private int col;
        private int tileIndex;  // Add tile index

        public TileState(String letter, int value, int row, int col, int tileIndex) {
            this.letter = letter;
            this.value = value;
            this.row = row;
            this.col = col;
            this.tileIndex = tileIndex;  // Initialize tile index
        }

        public String getLetter() { return letter; }

        public int getValue() { return value; }

        public int getRow() { return row; }

        public int getCol() { return col; }

        public int getTileIndex() { return tileIndex; }
    }
}
