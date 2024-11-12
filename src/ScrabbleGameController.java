import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.util.Stack;

public class ScrabbleGameController implements ActionListener {
    private ScrabbleGame model;
    private ScrabbleGameView view;
    private Tile selectedTile;  // To store the selected tile from the player's rack
    private Stack<TileState> history;  // Stack to store the previous state of board tiles

    public ScrabbleGameController(ScrabbleGame model, ScrabbleGameView view) {
        this.model = model;
        this.view = view;
        this.history = new Stack<>();  // Initialize the history stack
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String command = source.getActionCommand();
        System.out.println("Button clicked with action command: " + command); // Debugging

        // Handle tile button clicks (player's rack)
        if (command.startsWith("Tile")) {
            // Extract tile index (1-based to 0-based)
            int tileIndex = Integer.parseInt(command.split(" ")[1]) - 1;
            selectedTile = model.getPlayerTurn().getTiles().get(tileIndex);  // Get selected tile from player's rack
            System.out.println("Selected Tile: " + selectedTile.getLetter() + " with value: " + selectedTile.getValue());
        }

        // Handle board tile clicks
        else if (command.startsWith("BoardTile")) {
            // Get board tile position (can be a more complex mapping depending on how your board is set up)
            int rowIndex = Integer.parseInt(command.split(" ")[1]);
            int colIndex = Integer.parseInt(command.split(" ")[2]);

            // Save the previous tile state before updating
            savePreviousState(rowIndex, colIndex);

            // Update the square on the board with the selected tile (if a tile is selected)
            if (selectedTile != null) {
                // Call the editSquare method with row, col, tile letter, and tile value
                view.editSquare(rowIndex, colIndex, selectedTile.getLetter(), selectedTile.getValue());
                System.out.println("Board updated at (" + rowIndex + ", " + colIndex + ") with tile: " + selectedTile.getLetter());

                selectedTile = null;  // Clear the selected tile after placing it on the board
            }
        }

        // Handle "Clear" button action (Undo action)
        else if (command.equals("Clear")) {
            undoLastMove();  // Undo the last placed tile
        }

        // Handle "Play" button action (clear the history)
        else if (command.equals("Play")) {
            clearHistory();  // Clear the history stack when "Play" is pressed
            System.out.println("Game history cleared.");
        }
    }

    // Method to save the previous state of a tile before it is updated
    private void savePreviousState(int rowIndex, int colIndex) {
        String letter = view.getTileLetterAt(rowIndex, colIndex);
        int value = view.getTileValueAt(rowIndex, colIndex);
        TileState previousState = new TileState(letter, value, rowIndex, colIndex);
        history.push(previousState);  // Save the previous state to history stack
    }

    // Method to undo the last placed tile
    private void undoLastMove() {
        if (!history.isEmpty()) {
            TileState lastState = history.pop();  // Get the last state
            // Print the previous state (letter and value)
            System.out.println("Undid last move at (" + lastState.getRow() + ", " + lastState.getCol() + ")");
            System.out.println("Previously at this tile: Letter = " + lastState.getLetter() + ", Value = " + lastState.getValue());

            // Revert the tile on the board to its previous state
            view.editSquare(lastState.getRow(), lastState.getCol(), lastState.getLetter(), lastState.getValue());
        }
    }

    // Method to clear the history stack after the "Play" button is pressed
    private void clearHistory() {
        history.clear();
    }

    // Inner class to represent the state of a tile (letter, value, position)
    private class TileState {
        private String letter;
        private int value;
        private int row;
        private int col;

        public TileState(String letter, int value, int row, int col) {
            this.letter = letter;
            this.value = value;
            this.row = row;
            this.col = col;
        }

        public String getLetter() {
            return letter;
        }

        public int getValue() {
            return value;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
