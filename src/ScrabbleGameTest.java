import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScrabbleGameTest {
    ScrabbleGame game;
    ArrayList<Player> set1;
    ArrayList<Player> set2;
    ArrayList<Player> set3;

    // Test words
    ArrayList<Tile> read;
    ArrayList<Tile> been;
    ArrayList<Tile> ieee;
    ArrayList<Tile> aaa;
    ArrayList<Tile> piano;

    @org.junit.Test
    public void changeTurns() {
        game.setPlayerOrder(set3);
        game.setCurrentPlayerIndex(0);
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(1));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(2));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(3));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(0));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(1));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(2));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(3));
        game.changeTurns();
        assertEquals(game.getPlayerTurn(), set3.get(0));
    }

    @org.junit.Test
    public void playWord() {
        ScrabbleBoard board = game.getScrabbleBoard();
        Player p1 = new Player("Player 1");
        game.setPlayerInput("read 11 h");

    }

    @org.junit.Test
    public void firstPlayIsThroughCenter() {
    }

    @org.junit.Test
    public void allPlayWordsValid() {
    }

    @org.junit.Test
    public void putPlayOnBoard() {
    }

    @org.junit.Test
    public void removePlayFromBoard() {
    }

    @org.junit.Test
    public void determinePlayerTurns() {
    }

    @org.junit.Test
    public void parseInputPlay() {
    }

    @org.junit.Test
    public void isValidWord() {
    }

    @Before
    public void setUp() throws Exception {
        game = new ScrabbleGame();
        set1 = new ArrayList<>();
        set2 = new ArrayList<>();
        set3 = new ArrayList<>();

        read = new ArrayList<>();
        been = new ArrayList<>();
        ieee = new ArrayList<>();
        aaa = new ArrayList<>();
        piano = new ArrayList<>();

        game.setPlayers(set1);

        // Set up players
        set1.add(new Player("Player 1"));
        set1.add(new Player("Player 2"));

        set2.add(new Player("Player 1"));
        set2.add(new Player("Player 3"));
        set2.add(new Player("Player 2"));

        set3.add(new Player("Player 3"));
        set3.add(new Player("Player 1"));
        set3.add(new Player("Player 4"));
        set3.add(new Player("Player 2"));

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
