// File: ScrabbleGameEvent.java

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ScrabbleGameEvent class handles user interactions in the view and sends
 * the corresponding events to the controller.
 */
public class ScrabbleGameEvent implements ActionListener {
    private ScrabbleGame controller;

    /**
     * Constructor for the ScrabbleGameEvent class.
     * @param controller The controller that manages the game logic.
     */
    public ScrabbleGameEvent(ScrabbleGame controller) {
        this.controller = controller;
    }

    /**
     * Handles actions from the view and notifies the controller.
     * @param e The event triggered by the user action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Example: Handle tile placement
        if (command.startsWith("PLACE_TILE")) {
            // Extract row, column, and tile information from the command
            String[] parts = command.split(":");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            String letter = parts[3];
            int value = Integer.parseInt(parts[4]);

            Tile tile = new Tile(letter, value);
            //controller.placeTile(row, col, tile);
        }

        // Additional commands can be added here for other game actions.
    }
}
