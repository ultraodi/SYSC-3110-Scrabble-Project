import java.util.ArrayList;

public class Player {
    public ArrayList<Tile> getTiles() {
        Tile a = new Tile("A", 1);
        Tile p = new Tile("P", 1);
        Tile p2 = new Tile("L", 1);
        Tile l = new Tile("L", 1);
        Tile e = new Tile("E", 1);
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(a);
        tiles.add(p);
        tiles.add(p2);
        tiles.add(l);
        tiles.add(e);
        return  tiles;
    }
}
