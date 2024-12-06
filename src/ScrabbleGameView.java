import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * The ScrabbleGame class simulates as at this version a turn by turn text version of the
 * game Scrabble.
 * @author Abdul Babatunde
 * @author Clinton Odinamadu
 * @version November 12, 2024
 */

public class ScrabbleGameView extends JFrame implements Serializable {

    private JLabel tilesLeftLabel;
    private JLabel currentPlayerLabel;
    private JLabel currentPlayerScoreLabel;
    private JLabel AILabel;
    private JButton[][] buttons;
    private Square[][] squares;
    private JButton[] tileButtons = new JButton[7];
    public static final int SIZE = 15;
    ScrabbleGame model;
    ScrabbleGameController sgc;
    private TileBag tileBag;
    private JButton[] tileValueButtons = new JButton[7];
    private boolean isAI = false;
    private int BOARD_SIZE = 15;
    private JMenuBar menuBar;
    private JMenu editMenu;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenu saveLoadMenuItem;
    private JMenuItem saveCurrentState;
    private JMenuItem loadCurrentState;

    private JMenu quitMenu;

    private JMenuItem quitMenuItem;

    private Color currentColor;  // Initial text color
    private final Color[] colors = {Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE, Color.MAGENTA};  // Array of colors
    private int colorIndex = 0;  // To cycle through the colors

    public ScrabbleGameView() {
        super("Scrabble Game!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 1000);
        this.setResizable(true);
        this.setBackground(new Color(255, 230, 0));

        String selectedLayout = getBoardLayout();

        tileBag = new TileBag(selectedLayout);
        menuBar = new JMenuBar();

        currentColor = Color.GREEN;

        // Create the "Edit" menu
        editMenu = new JMenu("Undo/Redo");

        // Create the save/load menu
        saveLoadMenuItem = new JMenu("Save/Load");

        // Create "Undo" and "Redo" menu items
        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");

        // Create the "Save" and "Load" menu items
        saveCurrentState = new JMenuItem("Save");
        loadCurrentState = new JMenuItem("Load");

        quitMenu = new JMenu("Quit");
        quitMenuItem = new JMenuItem("QUIT");

        quitMenu.add(quitMenuItem);

        // Add Save/Load items to menu
        saveLoadMenuItem.add(saveCurrentState);
        saveLoadMenuItem.add(loadCurrentState);

        // Add action commands
        undoMenuItem.setActionCommand("Undo");
        redoMenuItem.setActionCommand("Redo");

        // Set keyboard shortcuts
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));

        // Add menu items to the "Edit" menu
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        // Add the "Edit" menu to the menu bar
        menuBar.add(editMenu);
        menuBar.add(saveLoadMenuItem);
        menuBar.add(quitMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);
    }

    public static String getBoardLayout() {
        String[] options = {"normal", "diamond"};
        String selectedLayout = (String) JOptionPane.showInputDialog(
                null,
                "Choose board layout:",
                "Select board layout",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selectedLayout == null) {
            JOptionPane.showMessageDialog(null, "No options selected", "No layout seleced", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
        System.out.println(selectedLayout);
        return selectedLayout;
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

        // Add action listeners
        undoMenuItem.addActionListener(sgc);
        redoMenuItem.addActionListener(sgc);

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
                button.setOpaque(true);
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
        playButton.setActionCommand("Play");  // Set the action command for the "Play" button
        passButton.setActionCommand("Pass");  // Set the action command for the "Pass" button
        swapButton.setActionCommand("Swap");  // Set the action command for the "Pass" button



        clearButton.addActionListener(sgc);  // Add the controller as the action listener
        playButton.addActionListener(sgc);  // Add the controller as the action listener
        passButton.addActionListener(sgc);  // Add the controller as the action listener
        swapButton.addActionListener(sgc);  // Add the controller as the action listener


        // Player tile values
        JPanel tilesValuePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        for (int i = 0; i < tileButtons.length; i++) {
            tileValueButtons[i] = new JButton("0");
            tileValueButtons[i].setPreferredSize(new Dimension(50, 50));
            tileValueButtons[i].setEnabled(false);
            tilesValuePanel.add(tileValueButtons[i]);
        }

        // Add new labels to the left of the tile buttons
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));  // Stack vertically

        if (isAI){
            tilesLeftLabel = new JLabel("Tiles left in bag: ");
            currentPlayerLabel = new JLabel("Current player: ");
            currentPlayerScoreLabel = new JLabel("Current player score: ");
            AILabel = new JLabel("AI score is ");
        }
        else{
            tilesLeftLabel = new JLabel("Tiles left in bag: ");
            currentPlayerLabel = new JLabel("Current player: ");
            currentPlayerScoreLabel = new JLabel("Current player score: ");
            AILabel = new JLabel("AI score is ");
        }


        // Set the font size for the labels
        Font labelFont = new Font("Arial", Font.BOLD, 18);  // Larger font size
        tilesLeftLabel.setFont(labelFont);
        currentPlayerLabel.setFont(labelFont);
        currentPlayerScoreLabel.setFont(labelFont);
        AILabel.setFont(labelFont);

        // Add bottom margin to create double spacing
        tilesLeftLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels
        currentPlayerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels
        currentPlayerScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels
        AILabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Add space between labels

        labelsPanel.add(tilesLeftLabel);
        labelsPanel.add(currentPlayerLabel);
        labelsPanel.add(currentPlayerScoreLabel);
        if (isAI){
            labelsPanel.add(AILabel);
        }

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.add(tilesValuePanel, BorderLayout.NORTH);
        sidePanel.add(tilesPanel, BorderLayout.SOUTH);

        // Combine the labelsPanel (left) and tilesPanel (right) horizontally in a new panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(labelsPanel, BorderLayout.WEST);// Place labelsPanel on the left
        bottomPanel.add(sidePanel, BorderLayout.CENTER);

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
        ScrabbleBoard board = model.getScrabbleBoard();
        Square square = board.getSquare(row, col);
        return square.getType();
    }


    public List<String> setupPlayersOptionPane() {
        boolean wantsAI = false;
        Object playWithAI = null;

        // Determine if the player wants to play with the AI
        while (!wantsAI) {
            JOptionPane wantsAIPane = new JOptionPane(
                    "Would you like to play with AI yes or no: ",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
            );
            wantsAIPane.setWantsInput(true);
            JDialog dialog = wantsAIPane.createDialog("Input");
            dialog.setVisible(true);

            playWithAI = wantsAIPane.getInputValue();
            if (playWithAI == JOptionPane.UNINITIALIZED_VALUE) {
                return null; // Dialog was closed
            }

            if (playWithAI.toString().toLowerCase().trim().equals("yes")) {
                wantsAI = true;
            } else if (playWithAI.toString().toLowerCase().trim().equals("no")) {
                wantsAI = true;
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid OPTION.");
            }
        }

        if (playWithAI.toString().toLowerCase().trim().equals("yes")) {
            // Setup with AI
            return setupPlayersWithAI();
        } else {
            // Setup without AI
            return setupPlayersWithoutAI();
        }
    }

    public List<String> setupPlayersWithAI() {
        List<String> playerNames = new ArrayList<>();
        isAI = true;
        String playerName = null;
        boolean validNameInput = false;

        while (!validNameInput) {
            JOptionPane playerPane = new JOptionPane(
                    "Player 1 "  + ": What is your name?",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
            );
            playerPane.setWantsInput(true);

            JDialog playerDialog = playerPane.createDialog("Player 1 " + " Details");
            playerDialog.setVisible(true);

            Object playerInput = playerPane.getInputValue();

            // Check if input is valid
            if (playerInput != null && !playerInput.toString().trim().isEmpty() && !playerInput.toString().trim().toUpperCase().equals("AI")) {
                playerName = playerInput.toString().trim();
                validNameInput = true;  // Exit the loop if input is valid
            } else if (playerInput.toString().trim().toUpperCase().equals("AI")) {
                JOptionPane.showMessageDialog(this, "AI name is reserved, please pick another name");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            }
        }

        playerNames.add(playerName); // Add validated name to player names list

        playerNames.add("AI"); // Adds the AI to the list of players

        return playerNames; // Return list of names

    }

    public List<String> setupPlayersWithoutAI() {
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

    // Serialized listener method
    public void addNewExportSerListener(ActionListener listener) {
        saveCurrentState.addActionListener(listener);
    }

    // Serialized view option to enter filename
    public String getSerializedExportFileName() {
        return JOptionPane.showInputDialog(this, "Enter File Name to Serialize: ");
    }

    // Serialized listener method
    public void addNewImportSerListener(ActionListener listener) {
        loadCurrentState.addActionListener(listener);
    }

    // Serialized view option to enter filename
    public String getSerializedImportFileName() {
        return JOptionPane.showInputDialog(this, "Enter File Name to Unserialize:");
    }

    // Quit Game listener method
    public void addNewQuitGameListener(ActionListener listener) {
        quitMenuItem.addActionListener(listener);
    }

    private String getPlayerName(int playerNumber) {
        String playerName = null;
        boolean validNameInput = false;

        while (!validNameInput) {
            JOptionPane playerPane = new JOptionPane(
                    "Player " + playerNumber + ": What is your name?",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
            );
            playerPane.setWantsInput(true);

            JDialog playerDialog = playerPane.createDialog("Player " + playerNumber + " Details");
            playerDialog.setVisible(true);

            Object playerInput = playerPane.getInputValue();

            // Check if input is valid
            if (playerInput != null && !playerInput.toString().trim().isEmpty() && !playerInput.toString().trim().toUpperCase().equals("AI")) {
                playerName = playerInput.toString().trim();
                validNameInput = true;  // Exit the loop if input is valid
            } else if (playerInput.toString().trim().toUpperCase().equals("AI")) {
                JOptionPane.showMessageDialog(this, "AI name is reserved, please pick another name");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            }
        }

        return playerName;
    }


    public void updateBlankTile(String letter, int value, int index) {
        if (model != null) {
            // Update labels with model data
            tilesLeftLabel.setText("Tiles left in bag: " + model.getTileBag().getNumTilesLeft());
            currentPlayerLabel.setText("Current player: " + model.getPlayerTurn().getPlayerName());
            currentPlayerScoreLabel.setText("Current player score: " + model.getPlayerTurn().getScore());

            if (isAI) {
                AILabel.setText("AI score is " + model.getPlayerByName("AI").getScore());
            }

            // Update the tile details at the specified index
            if (index >= 0 && index < tileButtons.length) {  // Ensure the index is within bounds
                tileButtons[index].setText(letter);
                tileValueButtons[index].setText(String.valueOf(value));
            } else {
                System.out.println("Index out of bounds.");
            }
        } else {
            System.out.println("Model is not initialized.");
        }
    }

    public boolean getIsAI(){
        return isAI;
    }

    public JButton getBoardSquare(int row, int col){
        return buttons[row][col];
    }

    public void setBoardSquare(JButton button, int row, int col){
        this.buttons[row][col] = button;
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
            if(isAI){
                // Check if the number of tiles is equal to 0
                if (model.getTileBag().getNumTilesLeft() == 0){
                    if (model.getPlayerWithHighestScore() != null){
                        celebrateWinner(model.getPlayerWithHighestScore().getPlayerName());
                    }
                    else{
                        celebrateWinner(null);
                    }
                    disableAllButtons();
                }
                else{
                    // Update labels with model data
                    tilesLeftLabel.setText("Tiles left in bag: " + model.getTileBag().getNumTilesLeft());
                    currentPlayerLabel.setText("Current player: " + model.getPlayerTurn().getPlayerName());
                    currentPlayerScoreLabel.setText("Current player score: " + model.getPlayerTurn().getScore());
                    AILabel.setText("AI score is " + model.getPlayerByName("AI").getScore());


                    // Print each tile to the console
                    String tilesString = model.getPlayerTurn().getTilesString();

                    for (int i = 0; i < tilesString.length(); i++) {
                        if (i < tileButtons.length) {  // Ensure we don’t exceed the number of buttons
                            tileButtons[i].setText(String.valueOf(tilesString.charAt(i)));
                            tileValueButtons[i].setText(String.valueOf(tileBag.getTileValue(String.valueOf(tilesString.charAt(i)))));
                        }
                    }
                }
            }
            else{
                if (model.getTileBag().getNumTilesLeft() == 0){
                    if (model.getPlayerWithHighestScore() != null){
                        celebrateWinner(model.getPlayerWithHighestScore().getPlayerName());
                    }
                    else{
                        celebrateWinner(null);
                    }
                    disableAllButtons();
                }
                else{
                    // Update labels with model data
                    tilesLeftLabel.setText("Tiles left in bag: " + model.getTileBag().getNumTilesLeft());
                    currentPlayerLabel.setText("Current player: " + model.getPlayerTurn().getPlayerName());
                    currentPlayerScoreLabel.setText("Current player score: " + model.getPlayerTurn().getScore());

                    // Print each tile to the console
                    String tilesString = model.getPlayerTurn().getTilesString();

                    for (int i = 0; i < tilesString.length(); i++) {
                        if (i < tileButtons.length) {  // Ensure we don’t exceed the number of buttons
                            tileButtons[i].setText(String.valueOf(tilesString.charAt(i)));
                            tileValueButtons[i].setText(String.valueOf(tileBag.getTileValue(String.valueOf(tilesString.charAt(i)))));
                        }
                    }
                }
            }

        } else {
            System.out.println("Model is not initialized.");
        }
    }


    public void enableAllTileButtons() {
        for (JButton button : tileButtons) {
            if (button != null) {
                button.setEnabled(true); // Re-enable each button in the rack
            }
        }
    }

    public Square[][] getSquares(){
        return this.squares;
    }

    public void setSquares(Square[][] squares){
        this.squares = squares;
    }

    public void updateBoard() {
        // First, check if the model is not null
        if (model == null) {
            System.out.println("Model is not initialized.");
            return;
        }

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

    // Function to make a board tile immutable
    public void disableBoardTile(int rowIndex, int colIndex) {
        // Check if the rowIndex and colIndex are within bounds
        if (rowIndex >= 0 && rowIndex < buttons.length && colIndex >= 0 && colIndex < buttons[0].length) {
            JButton button = buttons[rowIndex][colIndex];

            // Restore the button's properties
            button.setFocusable(true); // Allow the button to gain focus again
            button.setBackground(null); // Reset the background color to the default

            // Wait 1 second (1000ms) before setting the action command
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String command = "TwoClickBoardTile " + rowIndex + " " + colIndex;
                    button.setActionCommand(command);

                    // Attach the action listener after the delay
                    button.addActionListener(sgc);
                }
            });
            timer.setRepeats(false); // Ensure the timer only runs once
            timer.start();
        }
    }

    // Function to make a board tile mutable again
    public void enableBoardTile(int rowIndex, int colIndex) {
        // Check if the rowIndex and colIndex are within bounds
        if (rowIndex >= 0 && rowIndex < buttons.length && colIndex >= 0 && colIndex < buttons[0].length) {
            JButton button = buttons[rowIndex][colIndex];

            // Restore the button's properties
            button.setFocusable(true); // Allow the button to gain focus again
            button.setBackground(null); // Reset the background color to the default

            String command = "BoardTile " + rowIndex + " " + colIndex;
            button.setActionCommand(command);

            // Add back the original action listener (assuming `sgc` is your controller)
            button.addActionListener(sgc);

        } else {
            // Log or handle invalid tile coordinates
            // System.out.println("Invalid tile coordinates.");
        }
    }

    public void disableAllButtons() {
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons.length; col++) {
                if (buttons[row][col] != null) {
                    buttons[row][col].setEnabled(false);  // Disable the button
                }
            }
        }
    }


    // Method to display the celebration message
    public void celebrateWinner(String winnerName) {
        // Create a frame for the celebration
        JFrame celebrationFrame = new JFrame("Winner Celebration");
        celebrationFrame.setSize(1500, 1000);  // Set the frame size to 1500x1000
        celebrationFrame.setLocationRelativeTo(null);  // Center the frame
        celebrationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel to display the message
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the winner message
                g.setColor(currentColor);
                g.setFont(new Font("Arial", Font.BOLD, 100));  // Font size and style
                FontMetrics fm = g.getFontMetrics();

                // Determine the message based on whether winnerName is null
                String message = (winnerName == null) ? "It's a Tie!" : winnerName + " Wins!";

                int x = (getWidth() - fm.stringWidth(message)) / 2;  // Center horizontally
                int y = getHeight() / 2;  // Center vertically
                g.drawString(message, x, y);
            }
        };
        celebrationFrame.add(panel);

        // Timer to change the color of the text
        Timer colorChangeTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorIndex = (colorIndex + 1) % colors.length;  // Cycle through the colors
                currentColor = colors[colorIndex];
                panel.repaint();  // Repaint the panel to apply the new color
            }
        });
        colorChangeTimer.start();  // Start the timer

        // Make the frame visible
        celebrationFrame.setVisible(true);
    }

}
