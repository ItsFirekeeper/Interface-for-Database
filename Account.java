/**
 * Stores the account information.
 *
 * @author Angela Li
 *
 */
public class Account {
    public long accountNumber;
    public String name;
    public String address;
    public double creditLimit;
    public double balance;

    /**
     * Constructor for Account.
     */
    public Account() {
        this.accountNumber = 0L;
        this.name = "";
        this.address = "";
        this.creditLimit = 0D;
        this.balance = 0D;
    }

    @Override
    public String toString() {
        return "Account " + accountNumber + "\n" + "name " + name + "\n" + "address " + address + "\n" + "creditLimit "
                + creditLimit + "\n" + "balance " + balance;
    }

}
