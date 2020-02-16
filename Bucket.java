/**
 * Bucket made of Account objects and the bucket deleltion flag.
 *
 * @author Angela Li
 *
 */
public class Bucket {

    private Account currentAccount;
    private boolean deletion;

    /**
     * Constructor for Bucket.
     *
     * @param accountNumber : unique number associated with the current account.
     * @param name          : name of the account owner.
     * @param address       : address of the account owner.
     * @param creditLimit   : purchasing limit of the acccount owners credit card.
     * @param balance       : current balance on account owner's credit card.
     */
    public Bucket(long accountNumber, String name, String address, double creditLimit, double balance) {

        this.currentAccount = new Account();
        this.currentAccount.accountNumber = accountNumber;
        this.currentAccount.name = name;
        this.currentAccount.address = address;
        this.currentAccount.creditLimit = creditLimit;
        this.currentAccount.balance = balance;
        this.deletion = false;
    }

    public Account getAccount() {

        return this.currentAccount;
    }

    public void setAccount(Account acct) {
        this.currentAccount = acct;
    }

    public double getCreditLimit() {
        return this.currentAccount.creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.currentAccount.creditLimit = creditLimit;
    }

    public double getBalance() {
        return this.currentAccount.balance;
    }

    /**
     * Changes the balance by adding a positive or negative amount to the balance of
     * the account.
     *
     * @param balance : amount that is being modified on the balance.
     */
    public void setBalance(double balance) {
        this.currentAccount.balance += balance;
    }

    public boolean getFlag() {

        return this.deletion;
    }

    public void setFlag(boolean flag) {
        this.deletion = flag;
    }

}
