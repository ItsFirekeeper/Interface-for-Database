/**
 * Database of credit card account's using a hash table.
 *
 * @author Angela Li
 */
public class CCHash implements CCDatabase {
    private int fillLevelH; // fill level of the hash table
    private Bucket arrayHash[];

    /**
     * Constructor for CCSorted objects.
     */
    public CCHash() {

        this.fillLevelH = 0;
        this.arrayHash = new Bucket[101];
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
        if ((double) fillLevelH / arrayHash.length > 0.6) {
            resize();
        }
        int index = find(accountNumber);
        Bucket newAccount = new Bucket(accountNumber, name, address, creditLimit, balance);
        if ((arrayHash[index] == null) || (arrayHash[index].getFlag() == true)) {
            // if bucket is null or bucket is avaliable.
            index = find(accountNumber);
            arrayHash[index] = newAccount;
            fillLevelH++;
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
        int index = find(accountNumber);
        if (arrayHash[index] == null && arrayHash[index].getAccount().accountNumber == accountNumber) {
            arrayHash[index].setFlag(true);
            fillLevelH--;
            return true;
        } else {
            return false; // account does not already exist
        }
    }

    /**
     * Adjusts the credit limit of the account with the given account number if it
     * exists.
     *
     * @param accountNumber : unique number associated with the current account.
     * @param newLimit      : the new credit limit to be adjusted on the account.
     * @returns boolean : true, if the account exists and was modified; false,
     *          otherwise.
     */
    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {
        int index = find(accountNumber);
        if (arrayHash[index] != null && arrayHash[index].getAccount().accountNumber == accountNumber) {
            arrayHash[index].setCreditLimit(newLimit);
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
        int index = find(accountNumber);
        if (arrayHash[index] != null && arrayHash[index].getAccount().accountNumber == accountNumber) {
            return arrayHash[index].getAccount().toString();
        } else { // account does not already exist
            return (arrayHash[index].getAccount().toString() + "cannot be printed is it does not exist");
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
        int index = find(accountNumber);
        if (arrayHash[index] != null && arrayHash[index].getAccount().accountNumber == accountNumber) {
            if (arrayHash[index].getCreditLimit() > (arrayHash[index].getBalance() + price)) {
                arrayHash[index].setBalance(price);
                return true;
            } else {
                throw new Exception("Purchase is over this account's credit limit");
            }
        } else {
            return false; // account does not already exist
        }
    }

    /**
     * Performs a folding operation that hashes the account number by splitting a
     * long into 4 shorts, then multiplying each short by a power of 17 and then
     * added together.
     *
     * @param accountNumber : unique number associated with the current account.
     * @return integer : folded account number that will be used as the index.
     */
    int hash(long accountNumber) {
        short c4 = (short) (accountNumber % 10000);
        short c3 = (short) (((accountNumber - c4) % 100000000) / 10000);
        short c2 = (short) (((accountNumber - (c4 + c3)) % Math.pow(10, 12)) / Math.pow(10, 8));
        short c1 = (short) (((accountNumber - (c4 + c2 + c3)) % Math.pow(10, 16)) / Math.pow(10, 12));

        return (int) ((17 * c1) + (Math.pow(17, 2) * c2) + (Math.pow(17, 3) * c3) + (Math.pow(17, 4) * c4))
                % arrayHash.length;
    }

    /**
     * Searches the array for the index where element belongs by hashing the account
     * number and probing until either a bucket with a true deletion flag or a null
     * bucket is encountered. The search also stops if the account number is already
     * in the table.
     *
     * @param accountNumber : unique number associated with the current account.
     * @return index : the index where the element belongs or where the index where
     *         the element currently already is.
     */
    int find(long accountNumber) {
        int index = hash(accountNumber);
        double counter = 0.0; // counter for probing
        int probeIndex = 0;
        if (arrayHash[index] == null) { // immediately hashes to a null bucket
            return index;
        } else if (arrayHash[index].getAccount().accountNumber == accountNumber) {
            return index;
        } else { // probing the hash table until a null bucket
            do {
                counter++;
                probeIndex = (int) ((index + Math.pow(counter, 2.0)) % arrayHash.length);
                if (arrayHash[probeIndex] == null) {
                    return probeIndex;
                } else if (arrayHash[probeIndex].getAccount().accountNumber == accountNumber) {
                    return probeIndex;
                }
            } while (arrayHash[probeIndex] != null);
        }
        return index;
    }

    /**
     * Resizes the hash table with the next largest prime number as the new size.
     */
    private void resize() {
        int size = this.arrayHash.length * 2 + 1;
        while (!isPrime(size)) {
            size += 2;
        }
        Bucket[] temp = arrayHash;
        arrayHash = new Bucket[size];
        fillLevelH = 0;
        for (int i = 0; i < temp.length; i++) { // rehashing table
            if (temp[i] != null && temp[i].getFlag() == false) {
                createAccount(temp[i].getAccount().accountNumber, temp[i].getAccount().name,
                        temp[i].getAccount().address, temp[i].getAccount().creditLimit, temp[i].getAccount().balance);
            }
        }
        return;
    }

    /**
     * Checks if the integer is a prime number or not; i.e. has no other multiples
     * besides 1 and itself.
     *
     * @param size : the number in question.
     * @return boolean : true, if number is prime, false, if number is not prime.
     */
    private boolean isPrime(int size) {
        for (int divisor = 2; divisor < size; divisor++) {
            if (size % divisor == 0) {
                return false;
            }
        }
        return true;
    }
}
