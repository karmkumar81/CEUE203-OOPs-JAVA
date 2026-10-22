import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;

// =============================================
// PRACTICAL 14 - MiniBank Capstone
// Integrates: Repository, BankService, Exceptions,
// Menu, Thread-safe processing
// =============================================

// ---- Annotations ----
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Id {}

// ---- Custom Exceptions ----
class BankException extends Exception {
    BankException(String msg) { super(msg); }
}

class InsufficientFundsException extends BankException {
    private long shortfall;
    InsufficientFundsException(long shortfall) {
        super("Short by: " + shortfall);
        this.shortfall = shortfall;
    }
    long getShortfall() { return shortfall; }
}

class AccountNotFoundException extends BankException {
    AccountNotFoundException(String id) { super("Not found: " + id); }
}

class InvalidAmountException extends BankException {
    InvalidAmountException(String msg) { super(msg); }
}

// ---- Account Model ----
class Account {
    @Id
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private static long counter = 0;

    Account(String ownerName, long balance) {
        this.accountNumber = String.format("AC%04d", ++counter);
        this.ownerName = ownerName;
        this.balance = balance;
    }

    synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive.");
        balance += amount;
    }

    synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive.");
        if (balance < amount) throw new InsufficientFundsException(amount - balance);
        balance -= amount;
    }

    String getAccountNumber() { return accountNumber; }
    String getOwnerName()     { return ownerName; }
    long getBalance()         { return balance; }

    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

// ---- Generic Repository ----
class Repository<T> {
    private HashMap<String, T> store = new HashMap<>();

    T save(T entity) {
        try {
            for (Field f : entity.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    f.setAccessible(true);
                    store.put((String) f.get(entity), entity);
                    return entity;
                }
            }
        } catch (IllegalAccessException e) { throw new RuntimeException(e); }
        throw new RuntimeException("No @Id field");
    }

    T findById(String id) { return store.get(id); }
    boolean delete(String id) { return store.remove(id) != null; }
    List<T> findAll() { return new ArrayList<>(store.values()); }
    int size() { return store.size(); }
}

// ---- BankService ----
class BankService {
    private Repository<Account> repo = new Repository<>();

    Account openAccount(String name, long balance) throws InvalidAmountException {
        if (balance < 0) throw new InvalidAmountException("Opening balance cannot be negative.");
        Account acc = new Account(name, balance);
        repo.save(acc);
        return acc;
    }

    void deposit(String accountNumber, long amount) throws BankException {
        Account acc = repo.findById(accountNumber);
        if (acc == null) throw new AccountNotFoundException(accountNumber);
        acc.deposit(amount);
    }

    void withdraw(String accountNumber, long amount) throws BankException {
        Account acc = repo.findById(accountNumber);
        if (acc == null) throw new AccountNotFoundException(accountNumber);
        acc.withdraw(amount);
    }

    void transfer(String fromId, String toId, long amount) throws BankException {
        Account from = repo.findById(fromId);
        Account to   = repo.findById(toId);
        if (from == null) throw new AccountNotFoundException(fromId);
        if (to   == null) throw new AccountNotFoundException(toId);
        from.withdraw(amount);
        to.deposit(amount);
    }

    String statement(String accountNumber) throws AccountNotFoundException {
        Account acc = repo.findById(accountNumber);
        if (acc == null) throw new AccountNotFoundException(accountNumber);
        return "Account: " + acc.getAccountNumber()
             + "\nOwner:   " + acc.getOwnerName()
             + "\nBalance: " + acc.getBalance();
    }

    void listAccounts() {
        if (repo.size() == 0) { System.out.println("No accounts."); return; }
        for (Object a : repo.findAll()) System.out.println("  " + a);
    }

    Repository<Account> getRepository() { return repo; }
}

// ---- TransactionProcessor ----
class TransactionProcessor {
    private ExecutorService pool = Executors.newFixedThreadPool(4);

    void submit(Runnable task) { pool.execute(task); }

    void stop() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }
}

// ---- Main Menu ----
public class Main {

    static BankService bankService = new BankService();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("===================================");
        System.out.println("     Welcome to MiniBank v14       ");
        System.out.println("===================================");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Statement");
            System.out.println("6. List All Accounts");
            System.out.println("7. Run Batch (Thread Pool)");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: "); String name = sc.nextLine();
                    System.out.print("Opening balance: "); long bal = sc.nextLong();
                    try { Account a = bankService.openAccount(name, bal);
                          System.out.println("Opened: " + a); }
                    catch (BankException e) { System.out.println("Error: " + e.getMessage()); }
                }
                case 2 -> {
                    System.out.print("Account No: "); String id = sc.nextLine();
                    System.out.print("Amount: "); long amt = sc.nextLong();
                    try { bankService.deposit(id, amt); System.out.println("Deposited."); }
                    catch (BankException e) { System.out.println("Error: " + e.getMessage()); }
                }
                case 3 -> {
                    System.out.print("Account No: "); String id = sc.nextLine();
                    System.out.print("Amount: "); long amt = sc.nextLong();
                    try { bankService.withdraw(id, amt); System.out.println("Withdrawn."); }
                    catch (BankException e) { System.out.println("Error: " + e.getMessage()); }
                }
                case 4 -> {
                    System.out.print("From Account: "); String from = sc.nextLine();
                    System.out.print("To Account: ");   String to   = sc.nextLine();
                    System.out.print("Amount: ");       long amt     = sc.nextLong();
                    try { bankService.transfer(from, to, amt); System.out.println("Transferred."); }
                    catch (BankException e) { System.out.println("Error: " + e.getMessage()); }
                }
                case 5 -> {
                    System.out.print("Account No: "); String id = sc.nextLine();
                    try { System.out.println(bankService.statement(id)); }
                    catch (BankException e) { System.out.println("Error: " + e.getMessage()); }
                }
                case 6 -> bankService.listAccounts();
                case 7 -> runBatch();
                case 8 -> { System.out.println("Goodbye!"); running = false; }
                default -> System.out.println("Invalid option.");
            }
        }
        sc.close();
    }

    static void runBatch() throws Exception {
        System.out.println("Running batch of 5 concurrent deposits...");
        Account acc = new Account("BatchTest", 0);
        bankService.getRepository().save(acc);

        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 0; i < 5; i++) {
            processor.submit(() -> {
                try { acc.deposit(100); }
                catch (InvalidAmountException e) { System.out.println(e.getMessage()); }
            });
        }
        processor.stop();
        System.out.println("Batch done. Balance: " + acc.getBalance());
    }
}
