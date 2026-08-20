public class CurrentAccount extends Account {

    private long overdraftLimit;

    CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public double interestRate() { return 0.0; }

    public boolean canWithdraw(long amount) {
        return amount > 0 && (getBalance() - amount) >= -overdraftLimit;
    }
}
