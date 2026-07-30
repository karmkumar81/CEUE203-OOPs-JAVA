public class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;
    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    // Constructor with opening balance
    Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    // Constructor chaining - zero opening balance
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

    String getAccountNumber() { return accountNumber; }
    String getOwnerName()     { return ownerName; }
    long getBalance()         { return balance; }
    boolean isActive()        { return active; }

    public static void main(String[] args) {
        Customer c1 = new Customer("Riya", "riya@bank.com", "9876543210");
        System.out.println("Customer: " + c1.getCustomerId() + " | " + c1.getName() + " | " + c1.getEmail());

        Account[] accounts = {
            new Account("Riya", 5000),
            new Account("Rahul", 3000),
            new Account("Sara")
        };

        // Test deposit and withdraw on first account
        accounts[0].deposit(2000);
        System.out.println(accounts[0].getOwnerName() + " balance after deposit: " + accounts[0].getBalance());

        boolean w1 = accounts[0].withdraw(3000);
        System.out.println("Withdraw 3000: " + w1 + " | Balance: " + accounts[0].getBalance());

        boolean w2 = accounts[0].withdraw(10000);
        System.out.println("Withdraw 10000: " + w2 + " | Balance: " + accounts[0].getBalance());

        // Print all accounts
        System.out.println("\nAll Accounts:");
        for (Account a : accounts) {
            System.out.println(a.getAccountNumber() + " | " + a.getOwnerName() + " | " + a.getBalance());
        }
    }
}
