import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ScrabbleGameController implements ActionListener {
    private ScrabbleGame model;
    private ScrabbleGameView view;
    private Tile selectedTile;  // To store the selected tile from the player's rack
    private Stack<TileState> history;  // Stack to store the previous state of board tiles
    private List<Tile> playedTiles;  // List to store the tiles played on the board
    private int selectedTileIndex = -1;  // Initialize with -1 to indicate no tile selected
    private int firstRow = -1, firstCol = -1;  // Coordinates of the first letter placed
    private int lastRow = -1, lastCol = -1;    // Coordinates of the last letter placed


    public ScrabbleGameController(ScrabbleGame model, ScrabbleGameView view) {
        this.model = model;
        this.view = view;
        this.history = new Stack<>();  // Initialize the history stack
        this.playedTiles = new ArrayList<>();  // Initialize the list of played tiles
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String command = source.getActionCommand();
        System.out.println("Button clicked with action command: " + command); // Debugging

        // Handle tile button clicks (player's rack)
        if (command.startsWith("Tile")) {
            // Extract tile index (1-based to 0-based)
            selectedTileIndex = Integer.parseInt(command.split(" ")[1]) - 1;
            selectedTile = model.getPlayerTurn().getTiles().get(selectedTileIndex);  // Get selected tile from player's rack
            System.out.println("Selected Tile: " + selectedTile.getLetter() + " with value: " + selectedTile.getValue());
        }

        // Handle board tile clicks
        else if (command.startsWith("BoardTile")) {
            int rowIndex = Integer.parseInt(command.split(" ")[1]);
            int colIndex = Integer.parseInt(command.split(" ")[2]);

            savePreviousState(rowIndex, colIndex);

            if (selectedTile != null) {
                view.editSquare(rowIndex, colIndex, selectedTile.getLetter(), selectedTile.getValue());
                System.out.println("Board updated at (" + rowIndex + ", " + colIndex + ") with tile: " + selectedTile.getLetter());

                // Track the first placement
                if (firstRow == -1 && firstCol == -1) {
                    firstRow = rowIndex;
                    firstCol = colIndex;
                    System.out.println("First letter placed at: (" + firstRow + ", " + firstCol + ")");
                }

                // Update the last placement
                lastRow = rowIndex;
                lastCol = colIndex;
                System.out.println("Last letter placed at: (" + lastRow + ", " + lastCol + ")");

                // Add the selected tile to the played tiles list
                playedTiles.add(selectedTile);
                System.out.println("Played tiles: " + formatPlayedTiles());
                System.out.println("Word was placed " + determineWordDirection());


                if (selectedTileIndex != -1) {
                    // Disable the tile button at the correct index
                    JButton tileButton = view.getPlayerRackButton(selectedTileIndex);
                    tileButton.setEnabled(false);
                    System.out.println("Disabled the tile button at index: " + selectedTileIndex);
                    selectedTileIndex = -1;  // Reset the selectedTileIndex after use
                }

                selectedTile = null;  // Clear the selected tile after placing it on the board

            }
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
            System.out.println("Played tiles after clear: " + formatPlayedTiles());
            System.out.println("Cleared all moves.");
        }

        // Handle "Play" button action (clear the history)
        else if (command.equals("Play")) {
            if (!playedTiles.isEmpty()) {
                // Generate and print the placement string
                String placementString = generatePlacementString();
                System.out.println(placementString);
            } else {
                System.out.println("No tiles were played.");
            }

            clearHistory();  // Clear the history stack
            playedTiles.clear();  // Clear the played tiles list
            firstRow = -1;
            firstCol = -1;
            lastRow = -1;
            lastCol = -1;
            System.out.println("Game history cleared.");
        }
    }

    // Method to save the previous state of a tile before it is updated
    private void savePreviousState(int rowIndex, int colIndex) {
        String letter = view.getTileLetterAt(rowIndex, colIndex);
        int value = view.getTileValueAt(rowIndex, colIndex);
        TileState previousState = new TileState(letter, value, rowIndex, colIndex, selectedTileIndex);
        history.push(previousState);  // Save the previous state to history stack
    }

    // Method to undo the last placed tile
    private void undoLastMove() {
        if (!history.isEmpty()) {
            TileState lastState = history.pop();  // Get the last state
            System.out.println("Undid last move at (" + lastState.getRow() + ", " + lastState.getCol() + ")");
            System.out.println("Previously at this tile: Letter = " + lastState.getLetter() + ", Value = " + lastState.getValue());

            // Revert the board tile to its previous state
            view.editSquare(lastState.getRow(), lastState.getCol(), lastState.getLetter(), lastState.getValue());

            // Re-enable the corresponding tile button in the player's rack
            int tileIndex = lastState.getTileIndex();
            if (tileIndex != -1) {
                JButton tileButton = view.getPlayerRackButton(tileIndex);
                tileButton.setEnabled(true);
                System.out.println("Re-enabled the tile button at index: " + tileIndex);
            }
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
