import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleGameController implements ActionListener {
    private ScrabbleGame model;
    private ScrabbleGameView view;

    public ScrabbleGameController(ScrabbleGame model, ScrabbleGameView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Handle tile button clicks (player's rack)
        if (command.startsWith("Tile")) {
            int tileIndex = Integer.parseInt(command.split(" ")[1]) - 1;  // Extract tile index (1-based to 0-based)
            Tile selectedTile = model.getPlayerTurn().getTile(tileIndex);  // Get selected tile from player's rack

            // Print the selected tile's information to the console
            if (selectedTile != null) {
                System.out.println("Selected Tile: " + selectedTile.getLetter() + " with value: " + selectedTile.getValue());
            } else {
                System.out.println("Invalid tile selected.");
            }
        }
    }
}
