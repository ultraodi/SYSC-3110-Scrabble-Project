/**
 * The ScrabbleBoard class represents the board where the scrabble game is played.
 * @author Marvel Adotse-ogah
 * @version October 18, 2024
 */
public class ScrabbleBoard {
    private Square[][] board;
    public static final int SIZE = 15;

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

         */

        // Building the board
        board = new Square[SIZE][SIZE];

        // Set all squares as null
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;
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
            board[1 + i][13 - 1] = new Square(Square.Types.DOUBLE_WORD);
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

        // Normal Squares
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    board[i][j] = new Square(Square.Types.NORMAL);
                }
            }
        }
    }

    public void drawBoard() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        String boardString = "";
        boardString = boardString.concat("   |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |  A  |  B  |  C  |  D  |  E  |  F  |\n");

        for (int i = 1; i <= SIZE; i++) {

        }

        return boardString;
    }

}
