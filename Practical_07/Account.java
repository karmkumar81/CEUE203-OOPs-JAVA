// Account with @Id and @Positive annotations
public class Account {

    @Id
    private final String accountNumber;

    private String ownerName;

    @Positive
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

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    String getAccountNumber() { return accountNumber; }
    long getBalance()         { return balance; }
}
