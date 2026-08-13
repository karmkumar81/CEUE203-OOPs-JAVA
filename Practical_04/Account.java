public class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    Account(String ownerName) { this(ownerName, 0); }

    void deposit(long amount) { if (amount > 0) balance += amount; }

    boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) { balance -= amount; return true; }
        return false;
    }

    String getAccountNumber() { return accountNumber; }
    String getOwnerName()     { return ownerName; }
    long getBalance()         { return balance; }
}
