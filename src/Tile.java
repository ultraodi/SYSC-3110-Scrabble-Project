/**
 * The Tile class simulates the individual tiles that can be interacted with by the users.
 * @author Clinton Odinamadu
 * @author Abdul Babatunde
 * @author Marvel Adotse-ogah
 * @version October 20, 2024
 */

public class Tile {
    private String letter;
    private int value;

    public Tile(String letter, int value) {
        this.letter = letter.toUpperCase();
        this.value = value;
    }

    public String getLetter() {
        return letter;
    }

    public int getValue(){
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile t = (Tile) obj;
            return letter.equals(t.letter);
        }
        return false;
    }
}
