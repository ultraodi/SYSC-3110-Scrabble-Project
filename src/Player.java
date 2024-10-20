import java.util.ArrayList;

public class Player {

    // Tile Bag
    private TileBag tilebag;

    // Tiles
    private ArrayList<Tile> tiles;

    // Player Score
    private int score;

    // Players name
    private String name;

    private ArrayList<Integer> scoreHistory;

    // player constructor
    public Player(String name){
        this.name = name;
        scoreHistory= new ArrayList<>();
        score = 0;
        tilebag = new TileBag();
        tiles = new ArrayList<>();
    }

    // get Player Name
    public String getPlayerName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    //
    // players move
    //public ArrayList<Tile> playersMove(){}

    // get more tiles
    // public void getMoreTiles() {}

    //    public ArrayList<Tile> getTiles() {
    //        Tile a = new Tile("A", 1);
    //        Tile p = new Tile("P", 1);
    //        Tile p2 = new Tile("L", 1);
    //        Tile l = new Tile("L", 1);
    //        Tile e = new Tile("E", 1);
    //        ArrayList<Tile> tiles = new ArrayList<>();
    //        tiles.add(a);
    //        tiles.add(p);
    //        tiles.add(p2);
    //        tiles.add(l);
    //        tiles.add(e);
    //        return  tiles;
    //    }
}
