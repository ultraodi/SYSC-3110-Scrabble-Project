import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ScrabbleGameTest {
    ScrabbleGame game;
    ArrayList<Player> set2Players;
    ArrayList<Player> set3Players;
    ArrayList<Player> set4Players;

    // Test words
    ArrayList<Tile> read;
    ArrayList<Tile> been;
    ArrayList<Tile> ieee;
    ArrayList<Tile> aaa;
    ArrayList<Tile> piano;
    ArrayList<Tile> pool;

    @org.junit.Test
    public void testChangeTurns() {
        game.setPlayerOrder(set4Players);
        game.setCurrentPlayerIndex(0);
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(1));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(2));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(3));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(0));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(1));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(2));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(3));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set4Players.get(0));
    }

    @org.junit.Test
    public void testPlayWord() {
        int index = 0;
        Square[][] board = game.getScrabbleBoard().getBoard();
        game.setPlayerOrder(set2Players);
        game.setCurrentPlayerIndex(index);
        game.setPlayerTurn(set2Players.getFirst());

        /*

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
             6 |     |  #  |     |     |     |  #  |     |     |     | (P) |     |     |     |  #  |     |
               -------------------------------------------------------------------------------------------
             7 |     |     |  %  |     |     |     |  %  |     |  %  | (I) | (E) | (E) | (E) |     |     |
               -------------------------------------------------------------------------------------------
             8 |  @  |     |     |  %  |     |     |     | (R) | (E) | (A) | (D) |  %  |     |     |  @  |
               -------------------------------------------------------------------------------------------
             9 |     |     |  %  |     |     |     | (B) | (E) | (E) | (N) |     |     |  %  |     |     |
               -------------------------------------------------------------------------------------------
             A |     |  #  |     |     |     |  #  |     |     |     | (O) |     |     |     |  #  |     |
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

        game.setPlayerInput("read 88 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(read);
        assertTrue(game.playWord());
        assertEquals(board[7][7].getPlacedTile().getLetter(), "R");
        assertEquals(board[7][8].getPlacedTile().getLetter(), "E");
        assertEquals(board[7][9].getPlacedTile().getLetter(), "A");
        assertEquals(board[7][10].getPlacedTile().getLetter(), "D");


        game.changeTurns();
        game.setPlayerInput("piano 6a v");
        game.parseInputPlay();
        set2Players.get(index + 1).setPlayerTiles(piano);
        assertTrue(game.playWord());
        assertEquals(board[5][9].getPlacedTile().getLetter(), "P");
        assertEquals(board[6][9].getPlacedTile().getLetter(), "I");
        assertEquals(board[7][9].getPlacedTile().getLetter(), "A");
        assertEquals(board[8][9].getPlacedTile().getLetter(), "N");
        assertEquals(board[9][9].getPlacedTile().getLetter(), "O");


        game.changeTurns();
        game.setPlayerInput("been 97 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(been);
        assertTrue(game.playWord());
        assertEquals(board[8][6].getPlacedTile().getLetter(), "B");
        assertEquals(board[8][7].getPlacedTile().getLetter(), "E");
        assertEquals(board[8][8].getPlacedTile().getLetter(), "E");
        assertEquals(board[8][9].getPlacedTile().getLetter(), "N");


        game.changeTurns();
        game.setPlayerInput("ieee 7a h");
        game.parseInputPlay();
        set2Players.get(index + 1).setPlayerTiles(ieee);
        assertTrue(game.playWord());
        assertEquals(board[6][9].getPlacedTile().getLetter(), "I");
        assertEquals(board[6][10].getPlacedTile().getLetter(), "E");
        assertEquals(board[6][11].getPlacedTile().getLetter(), "E");
        assertEquals(board[6][12].getPlacedTile().getLetter(), "E");

        game.changeTurns();
        game.setPlayerInput("pool 6a h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(pool);
        assertFalse(game.playWord());
        assertEquals(board[5][9].getPlacedTile().getLetter(), "P");
        assertNull(board[5][10].getPlacedTile());
        assertNull(board[5][11].getPlacedTile());
        assertNull(board[5][12].getPlacedTile());
    }

    @org.junit.Test
    public void testFirstPlayIsThroughCenter() {
        int index = 0;
        ScrabbleBoard board = game.getScrabbleBoard();
        game.setPlayerOrder(set2Players);
        game.setCurrentPlayerIndex(index);
        game.setPlayerTurn(set2Players.getFirst());

        game.setPlayerInput("read 11 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(read);
        assertFalse(game.playWord());

        game.setPlayerInput("ieee ff h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(ieee);
        assertFalse(game.playWord());

        game.setPlayerInput("piano 89 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(piano);
        assertFalse(game.playWord());

        game.setPlayerInput("piano 87 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(piano);
        assertTrue(game.playWord());
    }

    @org.junit.Test
    public void testAllPlayWordsValid() {
    }

    @org.junit.Test
    public void testPutPlayOnBoard() {
        int index = 0;
        Square[][] board = game.getScrabbleBoard().getBoard();
        game.setPlayerOrder(set2Players);
        game.setCurrentPlayerIndex(index);
        game.setPlayerTurn(set2Players.getFirst());

        /*

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
             6 |     |  #  |     |     |     |  #  |     |     |     | (P) |     |     |     |  #  |     |
               -------------------------------------------------------------------------------------------
             7 |     |     |  %  |     |     |     |  %  |     |  %  | (I) | (E) | (E) | (E) |     |     |
               -------------------------------------------------------------------------------------------
             8 |  @  |     |     |  %  |     |     |     | (R) | (E) | (A) | (D) |  %  |     |     |  @  |
               -------------------------------------------------------------------------------------------
             9 |     |     |  %  |     |     |     | (B) | (E) | (E) | (N) |     |     |  %  |     |     |
               -------------------------------------------------------------------------------------------
             A |     |  #  |     |     |     |  #  |     |     |     | (O) |     |     |     |  #  |     |
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

        game.setPlayerInput("read 88 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(read);
        game.putPlayOnBoard(ScrabbleGame.Direction.HORIZONTAL, 8, 8);
        assertEquals(board[7][7].getPlacedTile().getLetter(), "R");
        assertEquals(board[7][8].getPlacedTile().getLetter(), "E");
        assertEquals(board[7][9].getPlacedTile().getLetter(), "A");
        assertEquals(board[7][10].getPlacedTile().getLetter(), "D");


        game.changeTurns();
        game.setPlayerInput("piano 6a v");
        game.parseInputPlay();
        set2Players.get(index + 1).setPlayerTiles(piano);
        game.putPlayOnBoard(ScrabbleGame.Direction.VERTICAL, 6, 10);
        assertEquals(board[5][9].getPlacedTile().getLetter(), "P");
        assertEquals(board[6][9].getPlacedTile().getLetter(), "I");
        assertEquals(board[7][9].getPlacedTile().getLetter(), "A");
        assertEquals(board[8][9].getPlacedTile().getLetter(), "N");
        assertEquals(board[9][9].getPlacedTile().getLetter(), "O");


        game.changeTurns();
        game.setPlayerInput("been 97 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(been);
        game.putPlayOnBoard(ScrabbleGame.Direction.HORIZONTAL, 9, 7);
        assertEquals(board[8][6].getPlacedTile().getLetter(), "B");
        assertEquals(board[8][7].getPlacedTile().getLetter(), "E");
        assertEquals(board[8][8].getPlacedTile().getLetter(), "E");
        assertEquals(board[8][9].getPlacedTile().getLetter(), "N");


        game.changeTurns();
        game.setPlayerInput("ieee 7a h");
        game.parseInputPlay();
        set2Players.get(index + 1).setPlayerTiles(ieee);
        game.putPlayOnBoard(ScrabbleGame.Direction.HORIZONTAL, 7, 10);
        assertEquals(board[6][9].getPlacedTile().getLetter(), "I");
        assertEquals(board[6][10].getPlacedTile().getLetter(), "E");
        assertEquals(board[6][11].getPlacedTile().getLetter(), "E");
        assertEquals(board[6][12].getPlacedTile().getLetter(), "E");

        game.changeTurns();
        game.setPlayerInput("pool 6a h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(pool);
        game.putPlayOnBoard(ScrabbleGame.Direction.HORIZONTAL, 6, 10);
        assertEquals(board[5][9].getPlacedTile().getLetter(), "P");
        assertEquals(board[5][10].getPlacedTile().getLetter(), "O");
        assertEquals(board[5][11].getPlacedTile().getLetter(), "O");
        assertEquals(board[5][12].getPlacedTile().getLetter(), "L");
    }

    @org.junit.Test
    public void testRemovePlayFromBoard() {

    }

    @org.junit.Test
    public void testDeterminePlayerTurns() {
    }

    @org.junit.Test
    public void testParseInputPlay() {
    }

    @org.junit.Test
    public void testIsValidWord() {
    }

    @Before
    public void setUp() throws Exception {
        game = new ScrabbleGame();
        set2Players = new ArrayList<>();
        set3Players = new ArrayList<>();
        set4Players = new ArrayList<>();

        read = new ArrayList<>();
        been = new ArrayList<>();
        ieee = new ArrayList<>();
        aaa = new ArrayList<>();
        piano = new ArrayList<>();
        pool = new ArrayList<>();

        game.setPlayers(set2Players);

        // Set up players
        set2Players.add(new Player("Player 1"));
        set2Players.add(new Player("Player 2"));

        set3Players.add(new Player("Player 1"));
        set3Players.add(new Player("Player 3"));
        set3Players.add(new Player("Player 2"));

        set4Players.add(new Player("Player 3"));
        set4Players.add(new Player("Player 1"));
        set4Players.add(new Player("Player 4"));
        set4Players.add(new Player("Player 2"));

        // Set up words
        read.add(new Tile("R", 1));
        read.add(new Tile("E", 1));
        read.add(new Tile("A", 1));
        read.add(new Tile("D", 1));

        been.add(new Tile("B", 1));
        been.add(new Tile("E", 1));
        been.add(new Tile("E", 1));
        been.add(new Tile("N", 1));

        ieee.add(new Tile("I", 1));
        ieee.add(new Tile("E", 1));
        ieee.add(new Tile("E", 1));
        ieee.add(new Tile("E", 1));

        aaa.add(new Tile("A", 1));
        aaa.add(new Tile("A", 1));
        aaa.add(new Tile("A", 1));

        piano.add(new Tile("P", 1));
        piano.add(new Tile("I", 1));
        piano.add(new Tile("A", 1));
        piano.add(new Tile("N", 1));
        piano.add(new Tile("O", 1));

        pool.add(new Tile("P", 1));
        pool.add(new Tile("O", 1));
        pool.add(new Tile("O", 1));
        pool.add(new Tile("L", 1));
    }
}
