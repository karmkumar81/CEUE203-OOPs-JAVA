import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

// Practical 11: Serialization, file I/O, NIO

// Serializable Account
class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private static long counter = 0;

    Account(String ownerName, long balance) {
        this.accountNumber = String.format("AC%04d", ++counter);
        this.ownerName = ownerName;
        this.balance = balance;
    }

    void deposit(long amount) { if (amount > 0) balance += amount; }
    void withdraw(long amount) { if (amount > 0 && balance >= amount) balance -= amount; }

    String getAccountNumber() { return accountNumber; }
    long getBalance()         { return balance; }

    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

// Saves and loads Account[] to/from a file
class StatePersister {

    static void save(Account[] accounts, Path file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
            oos.writeObject(accounts);
        }
    }

    static Account[] load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) ois.readObject();
        }
    }
}

// Appends a line to a transaction log file
class TransactionLog {

    static void append(Path file, String line) throws IOException {
        Files.write(file, (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}

// Reads log files and generates a report
class ReportGenerator {

    static void generateReport(Path logsDir, Path reportFile) throws IOException {
        long totalDeposits = 0, totalWithdrawals = 0;
        StringBuilder report = new StringBuilder();
        report.append("=== End of Day Report ===\n");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
            for (Path logFile : stream) {
                BasicFileAttributes attrs = Files.readAttributes(logFile, BasicFileAttributes.class);
                report.append("File: ").append(logFile.getFileName())
                      .append(" | Size: ").append(attrs.size()).append(" bytes\n");

                try (BufferedReader reader = Files.newBufferedReader(logFile)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 3) {
                            long amount = Long.parseLong(parts[2]);
                            if (parts[0].equals("DEPOSIT"))  totalDeposits    += amount;
                            if (parts[0].equals("WITHDRAW")) totalWithdrawals += amount;
                        }
                    }
                }
            }
        }

        report.append("Total Deposits:    ").append(totalDeposits).append("\n");
        report.append("Total Withdrawals: ").append(totalWithdrawals).append("\n");
        report.append("Net Change:        ").append(totalDeposits - totalWithdrawals).append("\n");

        Files.write(reportFile, report.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        System.out.println(report.toString());
    }
}

public class Main {

    public static void main(String[] args) throws Exception {
        Path dataDir = Path.of("data");
        Path logsDir = Path.of("data", "logs");
        Files.createDirectories(logsDir);

        // Create accounts
        Account[] accounts = {
            new Account("Riya", 5000),
            new Account("Rahul", 3000),
            new Account("Sara", 8000)
        };

        // Save accounts
        Path dataFile = dataDir.resolve("accounts.dat");
        StatePersister.save(accounts, dataFile);
        System.out.println("Accounts saved.");

        // Reload accounts
        Account[] loaded = StatePersister.load(dataFile);
        System.out.println("Reloaded accounts:");
        for (Account a : loaded) System.out.println("  " + a);

        // Log some transactions
        Path logFile = logsDir.resolve("transactions.log");
        TransactionLog.append(logFile, "DEPOSIT AC0001 500");
        TransactionLog.append(logFile, "WITHDRAW AC0002 200");
        TransactionLog.append(logFile, "DEPOSIT AC0003 1000");

        // Generate report
        System.out.println("\n--- Report ---");
        ReportGenerator.generateReport(logsDir, dataDir.resolve("report.txt"));
    }
}
