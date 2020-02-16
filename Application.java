import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

/**
 * Application being run by main.
 *
 * @author Angela Li
 */
public class Application {
    /**
     * Parses a text file with operands that implement the CCDatabase interface and
     * makes a database based on the structure.
     *
     * @param structure : Chosen structure type of CCDatabase
     */
    public void run(CCDatabase structure) {
        Instant start = null;
        Instant stop = null;
        try {
            String choice;
            File tests = new File("src/Test4.txt");
            Scanner input = new Scanner(tests);
            long accountNumber;
            String name;
            String address;
            double creditLimit;
            double balance;
            double price;
            while (input.hasNextLine()) {
                choice = input.nextLine();
                switch (choice) {
                    case "start":
                        start = Instant.now();
                        break;
                    case "stop":
                        stop = Instant.now();
                        break;
                    case "cre":
                        accountNumber = Long.parseLong(input.nextLine());
                        name = input.nextLine();
                        address = input.nextLine();
                        creditLimit = Double.valueOf(input.nextLine());
                        balance = Double.valueOf(input.nextLine());
                        structure.createAccount(accountNumber, name, address, creditLimit, balance);
                        break;
                    case "del":
                        accountNumber = Long.parseLong(input.nextLine());
                        structure.deleteAccount(accountNumber);
                        break;
                    case "lim":
                        accountNumber = Long.parseLong(input.nextLine());
                        creditLimit = Double.valueOf(input.nextLine());
                        structure.adjustCreditLimit(accountNumber, creditLimit);
                        break;
                    case "pur":
                        accountNumber = Long.parseLong(input.nextLine());
                        price = Double.valueOf(input.nextLine());
                        try {
                            structure.makePurchase(accountNumber, price);
                        } catch (Exception invalidFunds) {
                            // invalidFunds.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("Invalid text commands: " + choice);
                }
            }
            input.close();
            long time = Duration.between(start, stop).toMillis();
            System.out.println("Time: " + time);
        } catch (FileNotFoundException notOpen) {
            // notOpen.printStackTrace();
            return;
        }
        return;
    }

}