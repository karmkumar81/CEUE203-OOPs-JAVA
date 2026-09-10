// Custom exception hierarchy for MiniBank

// Base checked exception
class BankException extends Exception {
    BankException(String message) { super(message); }
}

// Thrown when balance is insufficient
class InsufficientFundsException extends BankException {
    private long shortfall;
    InsufficientFundsException(long shortfall) {
        super("Insufficient funds. Short by: " + shortfall);
        this.shortfall = shortfall;
    }
    long getShortfall() { return shortfall; }
}

// Thrown when account is not found
class AccountNotFoundException extends BankException {
    AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}

// Thrown when an invalid amount is provided
class InvalidAmountException extends BankException {
    InvalidAmountException(String message) { super(message); }
}

// Account class with exception-throwing methods
class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private static long counter = 0;

    Account(String ownerName, long openingBalance) {
        this.accountNumber = String.format("AC%04d", ++counter);
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive.");
        balance += amount;
    }

    void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive.");
        if (balance < amount) throw new InsufficientFundsException(amount - balance);
        balance -= amount;
    }

    void transfer(Account to, long amount) throws BankException {
        try {
            this.withdraw(amount);
            to.deposit(amount);
        } catch (InsufficientFundsException | InvalidAmountException e) {
            throw new BankException("Transfer failed: " + e.getMessage());
        } finally {
            System.out.println("Transfer attempt logged for account: " + accountNumber);
        }
    }

    String getAccountNumber() { return accountNumber; }
    long getBalance()         { return balance; }

    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

// A simple AutoCloseable resource example
class BankSession implements AutoCloseable {
    String sessionId;
    BankSession(String sessionId) {
        this.sessionId = sessionId;
        System.out.println("Session opened: " + sessionId);
    }
    public void close() {
        System.out.println("Session closed: " + sessionId);
    }
}

public class Main {

    public static void main(String[] args) {
        Account acc = new Account("Riya", 1000);

        // Test exceptions
        try {
            acc.deposit(500);
            System.out.println("After deposit: " + acc.getBalance());

            acc.withdraw(5000);  // will throw InsufficientFundsException
        } catch (InsufficientFundsException e) {
            System.out.println("ERROR: " + e.getMessage() + " (shortfall: " + e.getShortfall() + ")");
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Balance remains: " + acc.getBalance());
        }

        // try-with-resources
        System.out.println();
        try (BankSession session = new BankSession("S001")) {
            System.out.println("Using session: " + session.sessionId);
            acc.withdraw(100);
            System.out.println("After withdraw: " + acc.getBalance());
        } catch (BankException e) {
            System.out.println("Bank error: " + e.getMessage());
        }
    }
}
