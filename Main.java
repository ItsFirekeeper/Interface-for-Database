/**
 * Application runner.
 *
 * @author Angela Li
 */
public class Main {
    /**
     * Runs the current application with the chosen database structure type.
     *
     * @param args : None.
     */
    public static void main(String[] args) {

        CCDatabase structure = new CCHash(); // choice can be "new CCHash" or "new CCSorted"
        new Application().run(structure);
    }
}