import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

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
        ScrabbleBoard board = game.getScrabbleBoard();
        game.setPlayerOrder(set2Players);
        game.setCurrentPlayerIndex(index);
        game.setPlayerTurn(set2Players.getFirst());

        game.setPlayerInput("read 88 h");
        game.parseInputPlay();
        set2Players.get(index).setPlayerTiles(read);
        assertTrue(game.playWord());
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
    }
}