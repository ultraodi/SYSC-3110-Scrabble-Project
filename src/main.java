public class Main {
    public static void main(String[] args) {
        EditableTileBag tileBag = new EditableTileBag();
        tileBag.setFrequency("A", 10); // Example of editing frequency
        tileBag.setValue("A", 2); // Example of editing value
        tileBag.printTileBagToFile("TileBag.txt");
    }
}
