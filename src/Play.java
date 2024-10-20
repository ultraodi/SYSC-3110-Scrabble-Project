import java.util.ArrayList;

public class Play {
    private Player player;
    private ArrayList<Square> word;
    private int points;

    // Constructor accepting a Player object and a word (list of Squares)
    public Play(Player player, ArrayList<Square> word) {
        this.player = player; // Assign the passed Player object
        this.word = word;
        this.points = 0; // Initialize points, or calculate based on word, etc.
    }

    // Getter and setter methods, additional logic can be added here
    public Player getPlayer(){
        return player;
    }

    // public String getWord(){}

    public int calculatePoints(){
        int currScore = player.getScore() + points;
        player.setScore(currScore);
        return currScore;
    }
}