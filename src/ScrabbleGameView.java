import javax.swing.*;
import java.awt.*;

public class ScrabbleGameView extends JFrame {

    private JButton[][] buttons;
    private ScrabbleBoard board; // Assuming this class exists

    private JTextArea infoArea;
    private int playerScore;
    private String playerTiles;

    public ScrabbleGameView() {
        super("Scrabble Game!");
        board = new ScrabbleBoard();

        // Set up the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 1000); // Increased width for better layout

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScrabbleGameView());
    }
}
