/**
 * The Square class models the squares on a Scrabble Board.
 */
public class Square {
    /**
     * Represents all the possible types for the square such as
     * Triple Word, Triple Letter, Double Word, Double Letter, and the normal squares.
     */
    public enum Types {
        TRIPLE_WORD,
        TRIPLE_LETTER,
        DOUBLE_WORD,
        DOUBLE_LETTER,
        NORMAL
    }

    /**
     * The tile on this square on the board.
     */
    private Tile placedTile;
    /**
     * The variable that holds the square type (Double Word, Double Letter, e.t.c.) for a square on the board.
     */
    private Square.Types squareType;

    /**
     * Constructor for this Class.
     * @param type The type designated using the static variables defined in this class.
     */
    public Square(Square.Types type) {
        this.squareType = type;
        this.placedTile = null;
    }

    /**
     * Constructor for this Class. Creates a normal square.
     */
    public Square() {
        this.squareType = Types.NORMAL;
    }

    /**
     * Assigns the tile placed on this square.
     * @param tile The tile placed on the square.
     */
    public void placeTile(Tile tile) {
        this.placedTile = tile;
    }

    /**
     * Gets the type of this square.
     * @return The type of square.
     */
    public Square.Types getType() {
        return squareType;
    }
}
