/**
 * The ScrabbleBoard class represents the board where the scrabble game is played.
 * @author Marvel Adotse-ogah
 * @version October 18, 2024
 */
public class ScrabbleBoard {
    /** The array model of the scrabble board*/
    private Square[][] board;
    /** The dimension of the board. Since the board is a square the size is singular.*/
    public static final int SIZE = 15;

    /**
     * Constructor for the ScrabbleBoard.
     */
    public ScrabbleBoard() {
        /*
            The concept of the board.
            @ = Triple Word
            # = Triple Letter
            $ = Double Word
            % = Double Letter
            ' '(empty space) = Normal square

               |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |  A  |  B  |  C  |  D  |  E  |  F  |
               -------------------------------------------------------------------------------------------
             1 |  @  |     |     |  %  |     |     |     |  @  |     |     |     |  %  |     |     |  @  |
               -------------------------------------------------------------------------------------------
             2 |     |  $  |     |     |     |  #  |     |     |     |  #  |     |     |     |  $  |     |
               -------------------------------------------------------------------------------------------
             3 |     |     |  $  |     |     |     |  %  |     |  %  |     |     |     |  $  |     |     |
               -------------------------------------------------------------------------------------------
             4 |  %  |     |     |  $  |     |     |     |  %  |     |     |     |  $  |     |     |  %  |
               -------------------------------------------------------------------------------------------
             5 |     |     |     |     |  $  |     |     |     |     |     |  $  |     |     |     |     |
               -------------------------------------------------------------------------------------------
             6 |     |  #  |     |     |     |  #  |     |     |     |  #  |     |     |     |  #  |     |
               -------------------------------------------------------------------------------------------
             7 |     |     |  %  |     |     |     |  %  |     |  %  |     |     |     |  %  |     |     |
               -------------------------------------------------------------------------------------------
             8 |  @  |     |     |  %  |     |     |     |  +  |     |     |     |  %  |     |     |  @  |
               -------------------------------------------------------------------------------------------
             9 |     |     |  %  |     |     |     |  %  |     |  %  |     |     |     |  %  |     |     |
               -------------------------------------------------------------------------------------------
             A |     |  #  |     |     |     |  #  |     |     |     |  #  |     |     |     |  #  |     |
               -------------------------------------------------------------------------------------------
             B |     |     |     |     |  $  |     |     |     |     |     |  $  |     |     |     |     |
               -------------------------------------------------------------------------------------------
             C |  %  |     |     |  $  |     |     |     |  %  |     |     |     |  $  |     |     |  %  |
               -------------------------------------------------------------------------------------------
             D |     |     |  $  |     |     |     |  %  |     |  %  |     |     |     |  $  |     |     |
               -------------------------------------------------------------------------------------------
             E |     |  $  |     |     |     |  #  |     |     |     |  #  |     |     |     |  $  |     |
               -------------------------------------------------------------------------------------------
             F |  @  |     |     |  %  |     |     |     |  @  |     |     |     |  %  |     |     |  @  |
               -------------------------------------------------------------------------------------------

         */

        // Building the board
        board = new Square[SIZE][SIZE];

        // Set all squares as null
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = null;
            }
        }

        // Triple Word Squares
        board[0][0] = new Square(Square.Types.TRIPLE_WORD);
        board[7][0] = new Square(Square.Types.TRIPLE_WORD);
        board[14][0] = new Square(Square.Types.TRIPLE_WORD);
        board[0][7] = new Square(Square.Types.TRIPLE_WORD);
        board[0][14] = new Square(Square.Types.TRIPLE_WORD);
        board[7][14] = new Square(Square.Types.TRIPLE_WORD);
        board[14][7] = new Square(Square.Types.TRIPLE_WORD);
        board[14][14] = new Square(Square.Types.TRIPLE_WORD);

        // Triple Letter Squares
        board[5][1] = new Square(Square.Types.TRIPLE_LETTER);
        board[5][5] = new Square(Square.Types.TRIPLE_LETTER);
        board[5][9] = new Square(Square.Types.TRIPLE_LETTER);
        board[5][13] = new Square(Square.Types.TRIPLE_LETTER);
        board[9][1] = new Square(Square.Types.TRIPLE_LETTER);
        board[9][5] = new Square(Square.Types.TRIPLE_LETTER);
        board[9][9] = new Square(Square.Types.TRIPLE_LETTER);
        board[9][13] = new Square(Square.Types.TRIPLE_LETTER);
        board[1][5] = new Square(Square.Types.TRIPLE_LETTER);
        board[1][9] = new Square(Square.Types.TRIPLE_LETTER);
        board[13][5] = new Square(Square.Types.TRIPLE_LETTER);
        board[13][9] = new Square(Square.Types.TRIPLE_LETTER);

        // Double Word Squares
        for (int i = 0; i < 4; i++) {
            board[1 + i][1 + i] = new Square(Square.Types.DOUBLE_WORD);
            board[1 + i][13 - i] = new Square(Square.Types.DOUBLE_WORD);
            board[13 - i][1 + i] = new Square(Square.Types.DOUBLE_WORD);
            board[13 - i][13 - i] = new Square(Square.Types.DOUBLE_WORD);
        }

        // Double Letter Squares
        board[3][0] = new Square(Square.Types.DOUBLE_LETTER);
        board[11][0] = new Square(Square.Types.DOUBLE_LETTER);
        board[6][2] = new Square(Square.Types.DOUBLE_LETTER);
        board[8][2] = new Square(Square.Types.DOUBLE_LETTER);
        board[0][3] = new Square(Square.Types.DOUBLE_LETTER);
        board[7][3] = new Square(Square.Types.DOUBLE_LETTER);
        board[14][3] = new Square(Square.Types.DOUBLE_LETTER);
        board[2][6] = new Square(Square.Types.DOUBLE_LETTER);
        board[6][6] = new Square(Square.Types.DOUBLE_LETTER);
        board[8][6] = new Square(Square.Types.DOUBLE_LETTER);
        board[12][6] = new Square(Square.Types.DOUBLE_LETTER);
        board[3][7] = new Square(Square.Types.DOUBLE_LETTER);
        board[11][7] = new Square(Square.Types.DOUBLE_LETTER);
        board[3][14] = new Square(Square.Types.DOUBLE_LETTER);
        board[11][14] = new Square(Square.Types.DOUBLE_LETTER);
        board[6][12] = new Square(Square.Types.DOUBLE_LETTER);
        board[8][12] = new Square(Square.Types.DOUBLE_LETTER);
        board[0][11] = new Square(Square.Types.DOUBLE_LETTER);
        board[7][11] = new Square(Square.Types.DOUBLE_LETTER);
        board[14][11] = new Square(Square.Types.DOUBLE_LETTER);
        board[2][8] = new Square(Square.Types.DOUBLE_LETTER);
        board[6][8] = new Square(Square.Types.DOUBLE_LETTER);
        board[8][8] = new Square(Square.Types.DOUBLE_LETTER);
        board[12][8] = new Square(Square.Types.DOUBLE_LETTER);

        // Center Square
        board[7][7] = new Square(Square.Types.CENTER);

        // Normal Squares
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == null) {
                    board[row][col] = new Square(Square.Types.NORMAL);
                }
            }
        }
    }

    /**
     * Gets the tile on the square in the given position.
     * @param row The row location of the required square
     * @param col The column location of the required square
     * @return The tile placed on the square at the given position. Null if there is no tile on that square.
     */
    public Tile getTileOnSquare(int row, int col) {
        return board[row][col].getPlacedTile();
    }

    /**
     * Draw the board in the stdout area.
     */
    public void drawBoard() {
        System.out.println(this);
    }

    /**
     * A string representation of the board.
     * @return The string representation of the board.
     */
    @Override
    public String toString() {
        String stringRep = "";
        StringBuilder boardString = new StringBuilder();
        boardString.append("   |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |  A  |  B  |  C  |  D  |  E  |  F  |\n");

        for (int row = 1; row <= SIZE; row++) {
            boardString.append("   -------------------------------------------------------------------------------------------\n");
            boardString.append(" ".concat(Integer.toHexString(row).toUpperCase() + " |"));
            for (int col = 0; col < SIZE; col++) {
                Square.Types type = board[row - 1][col].getType();
                switch (type) {
                    case NORMAL:
                        stringRep = "   ";
                        break;
                    case DOUBLE_LETTER:
                        stringRep = "DLR";
                        break;
                    case DOUBLE_WORD:
                        stringRep = "DWD";
                        break;
                    case TRIPLE_LETTER:
                        stringRep = "TLR";
                        break;
                    case TRIPLE_WORD:
                        stringRep = "TWD";
                        break;
                    case CENTER:
                        stringRep = "[+]";
                        break;
                    default:
                        System.out.println("No Square type of that type.");
                }
                Square _square = board[row -1][col];
                boardString.append((_square.getPlacedTile() != null)? String.format(" [%s] |",_square.getPlacedTile().getLetter()) : String.format(" %s |", stringRep));
            }
            boardString.append("\n");
        }
        boardString.append("   -------------------------------------------------------------------------------------------\n");
        return boardString.toString();
    }

    public static void main(String[] args) {
        ScrabbleBoard board1 = new ScrabbleBoard();
        board1.drawBoard();
    }
}
