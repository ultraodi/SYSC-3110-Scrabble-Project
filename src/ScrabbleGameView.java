import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ScrabbleGameView extends JFrame {

    private JButton[][] buttons;
    private ScrabbleBoard board; // Assuming this class exists

    private JTextArea infoArea;
    private int playerScore;
    private String playerTiles;

    private JOptionPane numPlayersPane;

    public ScrabbleGameView() {
        super("Scrabble Game!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 1000); // Increased width for better layout
    }

    private void createBoard() {
        board = new ScrabbleBoard();

        // Create the main panel with a horizontal BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // Create the board panel with a grid of buttons
        JPanel boardPanel = new JPanel(new GridLayout(board.SIZE, board.SIZE));
        buttons = new JButton[board.SIZE][board.SIZE];

        for (int i = 0; i < board.SIZE; i++) {
            for (int j = 0; j < board.SIZE; j++) {
                JButton button = new JButton(" ");
                button.setActionCommand(i + " " + j);
                // button.addActionListener(controller); // Add your action listener
                buttons[i][j] = button;
                boardPanel.add(button); // Add button to board panel
            }
        }

        // Create the info area for displaying scores and player information
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        updateInfoArea(); // Initialize the text area with some info

        // Create a panel for the info area
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        infoPanel.setPreferredSize(new Dimension(300, 0)); // Fixed width for info panel

        // Add both panels to the main panel
        mainPanel.add(boardPanel); // Add board panel
        mainPanel.add(infoPanel); // Add info panel

        // Add the main panel to the frame
        this.add(mainPanel);
        this.setVisible(true);
    }

    private void updateInfoArea() {
        // Update the text area with the current game information
        StringBuilder info = new StringBuilder();
        info.append("Player Tiles: ").append(playerTiles).append("\n");
        info.append("Player Score: ").append(playerScore).append("\n");
        info.append("Total Points: ").append(playerScore).append("\n");
        infoArea.setText(info.toString());
    }

    public List<String> setupPlayersOptionPane() {
        List<String> playerNames = new ArrayList<>();

        int numPlayers = 0;
        boolean validInput = false;

        // Loop to get a valid number of players
        while (!validInput) {
            JOptionPane numPlayersPane = new JOptionPane(
                    "How many players will play (Lowest 2, Highest 4): ",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
            );
            numPlayersPane.setWantsInput(true);
            JDialog dialog = numPlayersPane.createDialog("Input");
            dialog.setVisible(true);

            // Retrieve and parse user input
            Object userInput = numPlayersPane.getInputValue();
            if (userInput == JOptionPane.UNINITIALIZED_VALUE) {
                return null; // Dialog was closed
            }

            try {
                numPlayers = Integer.parseInt(userInput.toString());
                if (numPlayers >= 2 && numPlayers <= 4) {
                    validInput = true; // Valid input received
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number between 2 and 4.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number between 2 and 4.");
            }
        }

        // Loop to get each player's name
        for (int i = 0; i < numPlayers; i++) {
            JOptionPane playerPane = new JOptionPane(
                    "Player " + (i + 1) + ": What is your name?",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
            );
            playerPane.setWantsInput(true);

            JDialog playerDialog = playerPane.createDialog("Player " + (i + 1) + " Details");
            playerDialog.setVisible(true);

            // Retrieve and store player name
            Object playerInput = playerPane.getInputValue();
            if (playerInput == JOptionPane.UNINITIALIZED_VALUE) {
                return null; // Dialog was closed
            }
            playerNames.add(playerInput != null ? playerInput.toString() : ""); // Add input or empty string
        }

        return playerNames; // Return list of names
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScrabbleGameView gameView = new ScrabbleGameView();
            List<String> playerNames = gameView.setupPlayersOptionPane();
            if (playerNames == null || playerNames.isEmpty()) {
                System.exit(0); // Terminate the application if setup failed or was closed
            }
            gameView.createBoard(); // Create the board only if setup is successful
        });
    }
}
