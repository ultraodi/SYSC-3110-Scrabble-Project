import java.util.ArrayList;
import java.util.HashSet;

public class testingConcepts {
    public static void main(String[] args) {
        Tile t1 = new Tile("t", 1);
        Tile i1 = new Tile("i", 1);
        Tile e1 = new Tile("e", 1);
        Tile t2 = new Tile("t", 1);
        Tile i2 = new Tile("i", 1);
        Tile e2 = new Tile("e", 1);

        Square s1 = new Square();
        Square s2 = new Square();
        Square s3 = new Square();
        Square s4 = new Square();
        Square s5 = new Square();
        Square s6 = new Square();

        s1.placeTile(t1);
        s2.placeTile(i1);
        s3.placeTile(e1);

        s4.placeTile(t2);
        s5.placeTile(i2);
        s6.placeTile(e2);

        ArrayList<Square> word1 = new ArrayList<>();
        ArrayList<Square> word2 = new ArrayList<>();
        word1.add(s1);
        word1.add(s2);
        word1.add(s3);
        word2.add(s4);
        word2.add(s5);
        word2.add(s6);

        Player p1 = new Player("nil");
        Player p2 = new Player("nil2");
        Player p3 = new Player("nil3");

        ArrayList<Play> plays = new ArrayList<>();

        Play py1 = new Play(p1, word1);
        Play py2 = new Play(p1, word2);
        Play py3 = new Play(p2, word1);
        Play py4 = new Play(p3, word2);

        plays.add(py1);
        plays.add(py2);

        System.out.println(py1.getWordString());
        System.out.println(py2.getWordString());
        System.out.println(py3.getWordString());
        System.out.println(plays.contains(py3));
    }
}
