/**
 * Database of credit card account's using a sorted array.
 *
 * @author Angela Li
 */
public class CCSorted implements CCDatabase {
    private int fillLevelS; // fill level of the sorted array
    private Account arraySorted[];

    /**
     * Constructor for CCSorted objects.
     */
    public CCSorted() {
        this.fillLevelS = 0;
        this.arraySorted = new Account[101];
    }

    /**
     * Creates an account with the given details if it does not exist.
     *
     * @param accountNumber : unique number associated with the current account.
     * @param name          : name of the account owner.
     * @param address       : address of the account owner.
     * @param creditLimit   : purchasing limit of the acccount owners credit card.
     * @param balance       : current balance on account owner's credit card.
     * @return boolean : indicates if the account was created, true, if created,
     *         false otherwise.
     */
    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {
        int place = findIndex(arraySorted, 0, fillLevelS, accountNumber);
        if (arraySorted[place] == null || (arraySorted[place].accountNumber != accountNumber)) {
            Account newAccount = new Account();
            newAccount.accountNumber = accountNumber;
            newAccount.name = name;
            newAccount.address = address;
            newAccount.creditLimit = creditLimit;
            newAccount.balance = balance;
            insertSorted(newAccount, place);
            return true;
        } else {
            return false; // account already exists
        }
    }

    /**
     * Deletes the account with the given account number if it does exist.
     *
     * @param accountNumber : unique number associated with the current account.
     * @return boolean : true, if an account was deleted; false, otherwise.
     */
    @Override
    public boolean deleteAccount(long accountNumber) {
        int place = findIndex(arraySorted, 0, fillLevelS, accountNumber);
        if (arraySorted[place] != null && arraySorted[place].accountNumber == accountNumber) {
            arraySorted[place] = null;
            while (place <= fillLevelS - 1) {
                arraySorted[place] = arraySorted[place + 1];
                place++;
            }
            fillLevelS--;
            return true;
        } else {
            return false; // account does not already exist
        }
    }

    /**
     * Inserts a new account into the sorted array by checking the fill level
     * percent with respects to the length of the array. if it's of 60% the array
     * will be resized before insertion. Then every index will be shifted down,
     * starting at the end and stopping where index where the new account should be
     * inserted. The fill level is then increased by 1.
     *
     * @param newAccount : the new account that will be inserted.
     * @param index      : the index where the new account belongs.
     */
    private void insertSorted(Account newAccount, int index) {
        if ((double) this.fillLevelS / arraySorted.length >= 0.6) { // resize if above 60% full
            resize();
        }
        int end = fillLevelS;
        if (fillLevelS != 0) {
            end -= 1;
        }
        while (!(end < index)) {
            arraySorted[end + 1] = arraySorted[end];
            end--;
        }
        this.arraySorted[index] = newAccount;
        fillLevelS++;
        return;
    }

    /**
     * Adjusts the credit limit of the account with the given account number if it
     * exists.
     *
     * @param accountNumber : unique number associated with the current account.
     * @param newLimit      : the new credit limit to be adjusted on the account.
     * @returns boolean : true if the account exists and was modified; false,
     *          otherwise.
     */
    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {
        int place = findIndex(arraySorted, 0, fillLevelS, accountNumber);
        if ((arraySorted[place] != null) && (arraySorted[place].accountNumber == accountNumber)) {
            arraySorted[place].creditLimit = newLimit;
            return true;
        } else {
            return false; // account does not already exist
        }
    }

    /**
     * Returns the details of the given account as a string if it exists or null if
     * the account does not exist. The string should be in the same format as for
     * account creation, i.e. the credit card number, name of card holder, address
     * of card holder, credit limit, and balance each on a separate line.
     *
     * @param accountNumber : unique number associated with the current account.
     * @return String : a string with either all account information or a error
     *         message if it doesn't exist.
     */
    @Override
    public String getAccount(long accountNumber) {
        int place = findIndex(arraySorted, 0, fillLevelS, accountNumber);
        if ((arraySorted[place] != null && arraySorted[place].accountNumber == accountNumber)) {
            return arraySorted[place].toString();
        } else {
            return (arraySorted[place].toString() + "cannot be printed is it does not exist");
        }
    }

    /**
     * Makes a purchase on the account with the given account number if it exists
     * and the account has sufficient credit. If the account exists, but has
     * insufficient funds (the old balance plus the purchase price is higher than
     * the account's credit limit), then an exception will be thrown.
     *
     * @param accountNumber : unique number associated with the current account.
     * @param price         : price of purchase being made.
     * @return boolean : true, if purchase went through and false, otherwise.
     * @throws Exception : if price plus balance exceeds the credit limit.
     */
    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {
        int place = findIndex(arraySorted, 0, fillLevelS, accountNumber);
        if (arraySorted[place] != null && arraySorted[place].accountNumber == accountNumber) {
            if (!(price + arraySorted[place].balance > arraySorted[place].creditLimit)) {
                arraySorted[place].balance = arraySorted[place].balance + price;
                return true;
            } else {
                throw new Exception("Purchase is over this account's credit limit and will be rejected");
            }
        } else {
            return false; // account does not already exist
        }

    }

    /**
     * A recursive binary search that looks for the index containing the account
     * number and either finds it or return the index where it belongs in the hash
     * table.
     *
     * @param databaseSorted : hash table of accounts.
     * @param start          : starting point for the search.
     * @param end            : end point for the search.
     * @param accountNumber  : unique number associated with the current account.
     * @return integer : representing either the index containing the account
     *         corresponding to the account number or the index where the account
     *         corresponding to the account number should be stored.
     */
    private int findIndex(Account databaseSorted[], int start, int end, long accountNumber) {
        if (start < end) {
            int mid = start + (end - start) / 2;
            if (accountNumber < databaseSorted[mid].accountNumber) {
                return findIndex(databaseSorted, start, mid, accountNumber);

            } else if (accountNumber > databaseSorted[mid].accountNumber) {
                return findIndex(databaseSorted, mid + 1, end, accountNumber);

            } else {
                return mid;
            }
        }
        return start;
    }

    /**
     * Resizes the array by making a new one double the size and then re-hashing all
     * buckets from the old one to the new, bigger array.
     */
    private void resize() {
        int size = this.arraySorted.length * 2;
        Account temp[] = new Account[size];
        for (int i = 0; i < fillLevelS; i++) {
            temp[i] = this.arraySorted[i];
        }
        this.arraySorted = temp;
    }
}