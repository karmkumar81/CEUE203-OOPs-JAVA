public class SavingsAccount extends Account {

    private long minBalance;

    SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }

    public double interestRate() { return 4.0; }

    public boolean canWithdraw(long amount) {
        return amount > 0 && (getBalance() - amount) >= minBalance;
    }
}
