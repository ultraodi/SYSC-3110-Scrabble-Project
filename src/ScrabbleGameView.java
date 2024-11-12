import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ScrabbleGameView extends JFrame {

    private JLabel tilesLeftLabel;
    private JLabel currentPlayerLabel;
    private JLabel currentPlayerScoreLabel;
    private JButton[][] buttons;
    private Square[][] squares;
    private JButton[] tileButtons = new JButton[7];
    public static final int SIZE = 15;
    ScrabbleGame model ;
    ScrabbleGameController sgc;

    public ScrabbleGameView() {
        super("Scrabble Game!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 1000);
    }

    // Move the board creation to a separate method
    public void initializeBoard() {
        createBoard();  // Only call this method after player setup
        this.setVisible(true);
        updateLabels();
    }

    // Setter method for the model
    public void setModel(ScrabbleGame model) {
        this.model = model;
        sgc = new ScrabbleGameController(model, this);  // Pass model if needed

    }

    private void createBoard() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Scrabble Board Panel (15x15 grid)
        JPanel boardPanel = new JPanel(new GridLayout(15, 15));
        buttons = new JButton[15][15];
        squares = new Square[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Square.Types type = getSquareType(i, j);  // Determine the square type based on position
                Square square = new Square(type);
                squares[i][j] = square;  // Store square object for each position

                JButton button = new JButton();
                button.setPreferredSize(new Dimension(40, 40));
                button.setMargin(new Insets(0, 0, 0, 0));
                setButtonDisplay(button, square);

                // Set the action command for the button, using row and column indices
                button.setActionCommand("BoardTile " + i + " " + j);

                // Add action listener using the controller
                button.addActionListener(sgc);

                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        // Player Tiles Panel (7 buttons for player's rack)
        JPanel tilesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        for (int i = 0; i < tileButtons.length; i++) {
            tileButtons[i] = new JButton("Tile " + (i + 1));
            tileButtons[i].setPreferredSize(new Dimension(50, 50));
            tileButtons[i].setActionCommand("Tile " + (i + 1)); // Set the action command
            tileButtons[i].addActionListener(sgc);  // Add the action listener here for tile buttons
            tilesPanel.add(tileButtons[i]);
        }

        // Gameplay Buttons (Play, Pass, Clear, Swap)
        JPanel gameplayButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        JButton playButton = new JButton("Play");
        JButton passButton = new JButton("Pass");
        JButton clearButton = new JButton("Clear");
        JButton swapButton = new JButton("Swap");

        gameplayButtonsPanel.add(playButton);
        gameplayButtonsPanel.add(passButton);
        gameplayButtonsPanel.add(clearButton);
        gameplayButtonsPanel.add(swapButton);


        clearButton.setActionCommand("Clear");  // Set the action command for the "Clear" button
        playButton.setActionCommand("Play");  // Set the action command for the "Clear" button


        clearButton.addActionListener(sgc);  // Add the controller as the action listener
        playButton.addActionListener(sgc);  // Add the controller as the action listener


        // Add new labels to the left of the tile buttons
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));  // Stack vertically
        tilesLeftLabel = new JLabel("Tiles left in bag: ");
        currentPlayerLabel = new JLabel("Current player: ");
        currentPlayerScoreLabel = new JLabel("Current player score: ");

        // Set the font size for the labels
        Font labelFont = new Font("Arial", Font.BOLD, 18);  // Larger font size
        tilesLeftLabel.setFont(labelFont);
        currentPlayerLabel.setFont(labelFont);
        currentPlayerScoreLabel.setFont(labelFont);

        // Add bottom margin to create double spacing
        tilesLeftLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels
        currentPlayerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels
        currentPlayerScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels

        labelsPanel.add(tilesLeftLabel);
        labelsPanel.add(currentPlayerLabel);
        labelsPanel.add(currentPlayerScoreLabel);

        // Combine the labelsPanel (left) and tilesPanel (right) horizontally in a new panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(labelsPanel, BorderLayout.WEST);  // Place labelsPanel on the left
        bottomPanel.add(tilesPanel, BorderLayout.CENTER); // Place tilesPanel in the center

        // Gameplay buttons panel added to the right of the tiles panel
        bottomPanel.add(gameplayButtonsPanel, BorderLayout.SOUTH);  // Align gameplay buttons to the right of tiles

        // Add panels to main panel
        mainPanel.add(boardPanel, BorderLayout.CENTER);  // Board in center
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);   // Bottom panel with tiles, labels, and gameplay buttons

        this.add(mainPanel);
    }

    /**
     * Sets the display properties for a button based on the Square object.
     * @param button The button to set.
     * @param square The square associated with the button.
     */
    private void setButtonDisplay(JButton button, Square square) {
        if (square.getPlacedTile() != null) {
            button.setText(String.valueOf(square.getPlacedTile().getLetter()));
            button.setBackground(Color.WHITE);
        } else {
            button.setText("");
            switch (square.getType()) {
                case TRIPLE_WORD -> {
                    button.setBackground(Color.RED);
                    button.setText("TWD");
                }
                case DOUBLE_WORD -> {
                    button.setBackground(Color.YELLOW);
                    button.setText("DWD");
                }
                case TRIPLE_LETTER -> {
                    button.setBackground(Color.BLUE);
                    button.setText("TLR");
                }
                case DOUBLE_LETTER -> {
                    button.setBackground(Color.GREEN);
                    button.setText("DLR");
                }
                case CENTER -> {
                    button.setBackground(Color.MAGENTA);
                    button.setText("+");
                }
                case NORMAL -> button.setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    /**
     * Determines the type of square based on its position.
     * This method can be customized for actual Scrabble board layout.
     */
    private Square.Types getSquareType(int row, int col) {
        // Triple Word Squares
        if ((row == 0 && col == 0) || (row == 7 && col == 0) || (row == 14 && col == 0) ||
                (row == 0 && col == 7) || (row == 0 && col == 14) || (row == 7 && col == 14) ||
                (row == 14 && col == 7) || (row == 14 && col == 14)) {
            return Square.Types.TRIPLE_WORD;
        }

        // Triple Letter Squares
        if ((row == 5 && col == 1) || (row == 5 && col == 5) || (row == 5 && col == 9) || (row == 5 && col == 13) ||
                (row == 9 && col == 1) || (row == 9 && col == 5) || (row == 9 && col == 9) || (row == 9 && col == 13) ||
                (row == 1 && col == 5) || (row == 1 && col == 9) || (row == 13 && col == 5) || (row == 13 && col == 9)) {
            return Square.Types.TRIPLE_LETTER;
        }

        // Double Word Squares (diagonal positions)
        if ((row == 1 && col == 1) || (row == 2 && col == 2) || (row == 3 && col == 3) || (row == 4 && col == 4) ||
                (row == 1 && col == 13) || (row == 2 && col == 12) || (row == 3 && col == 11) || (row == 4 && col == 10) ||
                (row == 13 && col == 1) || (row == 12 && col == 2) || (row == 11 && col == 3) || (row == 10 && col == 4) ||
                (row == 13 && col == 13) || (row == 12 && col == 12) || (row == 11 && col == 11) || (row == 10 && col == 10)) {
            return Square.Types.DOUBLE_WORD;
        }

        // Double Letter Squares
        if ((row == 3 && col == 0) || (row == 11 && col == 0) || (row == 6 && col == 2) || (row == 8 && col == 2) ||
                (row == 0 && col == 3) || (row == 7 && col == 3) || (row == 14 && col == 3) || (row == 2 && col == 6) ||
                (row == 6 && col == 6) || (row == 8 && col == 6) || (row == 12 && col == 6) || (row == 3 && col == 7) ||
                (row == 11 && col == 7) || (row == 3 && col == 14) || (row == 11 && col == 14) || (row == 6 && col == 12) ||
                (row == 8 && col == 12) || (row == 0 && col == 11) || (row == 7 && col == 11) || (row == 14 && col == 11) ||
                (row == 2 && col == 8) || (row == 6 && col == 8) || (row == 8 && col == 8) || (row == 12 && col == 8)) {
            return Square.Types.DOUBLE_LETTER;
        }

        // Center Square
        if (row == 7 && col == 7) {
            return Square.Types.CENTER;
        }

        // Normal Squares
        return Square.Types.NORMAL;
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
            String playerName = null;
            boolean validNameInput = false;

            while (!validNameInput) {
                JOptionPane playerPane = new JOptionPane(
                        "Player " + (i + 1) + ": What is your name?",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.OK_CANCEL_OPTION
                );
                playerPane.setWantsInput(true);

                JDialog playerDialog = playerPane.createDialog("Player " + (i + 1) + " Details");
                playerDialog.setVisible(true);

                Object playerInput = playerPane.getInputValue();

                // Check if input is valid
                if (playerInput != null && !playerInput.toString().trim().isEmpty()) {
                    playerName = playerInput.toString().trim();
                    validNameInput = true;  // Exit the loop if input is valid
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid name.");
                }
            }

            playerNames.add(playerName); // Add validated name to player names list
        }

        return playerNames; // Return list of names
    }

    public void editSquare(int row, int col, String tileLetter, int tileValue) {
        // Get the square and the button at the specified position
        Square square = squares[row][col];
        JButton button = buttons[row][col];

        // Create a new tile with the specified letter and value
        Tile newTile = new Tile(tileLetter, tileValue);

        // Place the new tile on the square
        square.placeTile(newTile);

        // Update the button text to reflect the new tile
        button.setText(String.valueOf(newTile.getLetter()));  // Set the button text to the tile letter

        // Update the button display (color and label) based on the square type
        setButtonDisplay(button, square);

        // Optionally, update any other elements if needed (like the player’s tiles)
        updateBoard();
    }

    public void updateLabels() {
        if (model != null) {
            // Update labels with model data
            tilesLeftLabel.setText("Tiles left in bag: " + model.getTileBag().getNumTilesLeft());
            currentPlayerLabel.setText("Current player: " + model.getPlayerTurn().getPlayerName());
            currentPlayerScoreLabel.setText("Current player score: " + model.getPlayerTurn().getScore());

            // Print each tile to the console
            String tilesString = model.getPlayerTurn().getTilesString();
            for (int i = 0; i < tilesString.length(); i++) {
                if (i < tileButtons.length) {  // Ensure we don’t exceed the number of buttons
                    tileButtons[i].setText(String.valueOf(tilesString.charAt(i)));
                }
            }
        } else {
            System.out.println("Model is not initialized.");
        }
    }

    public void updateBoard() {
        // First, check if the model is not null
        if (model == null) {
            System.out.println("Model is not initialized.");
            return;
        }

        // Get the current player's tiles and their positions
        List<Tile> playerTiles = model.getPlayerTurn().getTiles();

        // Loop through all the squares on the board
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                // Get the square and its corresponding button
                Square square = squares[row][col];
                JButton button = buttons[row][col];

                // Check if the square already has a placed tile
                Tile placedTile = square.getPlacedTile();

                if (placedTile != null) {
                    // If a tile is placed on this square, set the button text to the tile's letter
                    button.setText(String.valueOf(placedTile.getLetter()));
                } else {
                    // If no tile is placed, clear the button
                    button.setText("");
                }
            }
        }

        // Re-enable or reset any special board square labels if needed
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Square square = squares[row][col];
                JButton button = buttons[row][col];

                setButtonDisplay(button, square); // Update button display based on square type
            }
        }

        // Ensure the layout is updated
        SwingUtilities.invokeLater(() -> {
            // Revalidate and repaint the panel that holds the board
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    JButton button = buttons[row][col];
                    button.revalidate();
                    button.repaint();
                }
            }
        });
    }

    public int getTileValueAt(int rowIndex, int colIndex) {
        // Retrieve the square at the specified row and column
        Square square = squares[rowIndex][colIndex];

        // Check if a tile is placed on the square
        Tile placedTile = square.getPlacedTile();

        // If a tile is placed, return its value, otherwise return 0 (empty square)
        if (placedTile != null) {
            return placedTile.getValue();  // Get the value of the tile
        } else {
            return 0;  // No tile placed, return 0
        }
    }

    public String getTileLetterAt(int rowIndex, int colIndex) {
        // Get the button at the specified row and column
        JButton button = buttons[rowIndex][colIndex];

        // Return the text of the button, which is either the tile letter or an empty string
        return button.getText();
    }

    public JButton getPlayerRackButton(int index) { return tileButtons[index];}

    // Function to disable a board tile button
    public void disableBoardTile(int rowIndex, int colIndex) {
        // Check if the rowIndex and colIndex are within bounds
        if (rowIndex >= 0 && rowIndex < buttons.length && colIndex >= 0 && colIndex < buttons[0].length) {
            buttons[rowIndex][colIndex].removeActionListener(sgc);  // Disable the button at the specified row and column
            //System.out.println("Disabled the board tile at (" + rowIndex + ", " + colIndex + ")");
        } else {
            //System.out.println("Invalid tile coordinates.");
        }
    }

    // Function to disable a board tile button
    public void enableBoardTile(int rowIndex, int colIndex) {
        // Check if the rowIndex and colIndex are within bounds
        if (rowIndex >= 0 && rowIndex < buttons.length && colIndex >= 0 && colIndex < buttons[0].length) {
            buttons[rowIndex][colIndex].addActionListener(sgc);  // Disable the button at the specified row and column
            //System.out.println("Disabled the board tile at (" + rowIndex + ", " + colIndex + ")");
        } else {
            //System.out.println("Invalid tile coordinates.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScrabbleGameView gameView = new ScrabbleGameView();
            List<String> playerNames = gameView.setupPlayersOptionPane();
            if (playerNames == null || playerNames.isEmpty()) {
                System.exit(0); // Terminate the application if setup failed or was closed
            }

            // Create the board only after setup is successful
            gameView.initializeBoard();
        });
    }


}