// Practical 6 - All in one file for simplicity
// Demonstrates interfaces, functional interfaces, lambdas, anonymous classes, marker interface

// Functional interface
@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

// Marker interface
interface Premium {}

// Interface Transactable
interface Transactable {
    void deposit(long amount);
    boolean withdraw(long amount);
}

// Interface InterestBearing with default method
interface InterestBearing {
    double interestRate();

    default double yearlyInterest(long balance) {
        return balance * interestRate() / 100.0;
    }
}

// Simple Account class implementing interfaces
class Account implements Transactable, InterestBearing, Premium {

    private String accountNumber;
    private String ownerName;
    private long balance;
    private static long counter = 0;

    Account(String ownerName, long openingBalance) {
        this.accountNumber = String.format("AC%04d", ++counter);
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    public void deposit(long amount)  { if (amount > 0) balance += amount; }

    public boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) { balance -= amount; return true; }
        return false;
    }

    public double interestRate() { return 4.0; }

    public String getAccountNumber() { return accountNumber; }
    public long getBalance()         { return balance; }

    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

public class Main {

    public static void main(String[] args) {
        Account acc = new Account("Riya", 10000);
        System.out.println(acc);

        // WithdrawRule as anonymous class
        WithdrawRule ruleAnon = new WithdrawRule() {
            public boolean allow(Account account, long amount) {
                return account.getBalance() >= amount;
            }
        };

        // WithdrawRule as lambda
        WithdrawRule ruleLambda = (account, amount) -> account.getBalance() >= amount;

        System.out.println("Allow withdraw 5000 (anon):   " + ruleAnon.allow(acc, 5000));
        System.out.println("Allow withdraw 15000 (lambda): " + ruleLambda.allow(acc, 15000));

        // InterestBearing default method
        System.out.printf("Yearly interest: %.2f%n", acc.yearlyInterest(acc.getBalance()));

        // Marker interface check
        System.out.println("Is Premium? " + (acc instanceof Premium));
    }
}
