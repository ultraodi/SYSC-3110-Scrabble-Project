/**
 * The Square class models the squares on a Scrabble Board.
 */
public class Square {
    /**
     * Represent the scrabble Triple Word (TW) square on the board.
     */
    public static final int TRIPLE_WORD = 4;
    /**
     * Represent the scrabble Triple Letter (TL) square on the board.
     */
    public static final int TRIPLE_LETTER = 3;
    /**
     * Represent the scrabble Double Word (DW) square on the board.
     */
    public static final int DOUBLE_WORD = 2;
    /**
     * Represent the scrabble Double Letter (DL) square on the board.
     */
    public static final int DOUBLE_LETTER = 1;
    /**
     * Represent the scrabble normal square on the board. A square with not special
     * rules.
     */
    public static final int NORMAL = 0;


    private Tile placedTile;
    private int squareType;

    public Square(int type) {
        this.squareType = type;
    }

    public Square() {
        this.squareType = NORMAL;
    }

    public void placeTile(Tile tile) {
        this.placedTile = tile;
    }

    public int getType() {
        return squareType;
    }
}
