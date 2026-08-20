public class FixedDepositAccount extends Account {

    FixedDepositAccount(String ownerName, long openingBalance) {
        super(ownerName, openingBalance);
    }

    public double interestRate() { return 7.0; }

    public boolean canWithdraw(long amount) {
        System.out.println("Fixed deposit is locked. Cannot withdraw.");
        return false;
    }
}
