// Abstract base class Account
public abstract class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private static long counter = 0;

    private static String generateAccountNumber() {
        return String.format("AC%04d", ++counter);
    }

    Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    Account(String ownerName) { this(ownerName, 0); }

    // Abstract methods - subclasses must implement
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    void deposit(long amount) { if (amount > 0) balance += amount; }

    boolean withdraw(long amount) {
        if (canWithdraw(amount)) { balance -= amount; return true; }
        return false;
    }

    String getAccountNumber() { return accountNumber; }
    String getOwnerName()     { return ownerName; }
    long getBalance()         { return balance; }

    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance + " | Rate: " + interestRate() + "%";
    }
}
