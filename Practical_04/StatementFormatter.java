public class StatementFormatter {

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================\n");
        sb.append("Account Statement\n");
        sb.append("=====================================\n");
        sb.append("Account No : ").append(account.getAccountNumber()).append("\n");
        sb.append("Owner      : ").append(account.getOwnerName()).append("\n");
        sb.append("Balance    : ").append(account.getBalance()).append("\n");
        sb.append("=====================================\n");
        return sb.toString();
    }
}
