import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TileButtonActionListener implements ActionListener {
    private ScrabbleGame model;
    private ScrabbleGameView view;

    public TileButtonActionListener(ScrabbleGame model, ScrabbleGameView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //JButton source = (JButton) e.getSource();
        // Handle tile button click (e.g., add tile to board or swap tile)
    }
}
