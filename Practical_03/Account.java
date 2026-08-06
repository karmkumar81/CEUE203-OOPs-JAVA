import java.util.Objects;

public class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;
    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    Account(String ownerName) {
        this(ownerName, 0);
    }

    void deposit(long amount) {
        if (amount > 0) balance += amount;
    }

    boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // Practical 3: Override toString
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    // Practical 3: Override equals and hashCode by accountNumber
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;
        Account other = (Account) obj;
        return this.accountNumber.equals(other.accountNumber);
    }

    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    String getAccountNumber() { return accountNumber; }
    String getOwnerName()     { return ownerName; }
    long getBalance()         { return balance; }
    boolean isActive()        { return active; }
}
