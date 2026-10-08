import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Practical 12: Collections Framework - HashMap, ArrayList, Comparable, Comparator, ConcurrentHashMap

// Account implementing Comparable<Account>
class Account implements Comparable<Account> {

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

    // Comparable: sort by account number
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    String getAccountNumber() { return accountNumber; }
    String getOwnerName()     { return ownerName; }
    long getBalance()         { return balance; }

    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + balance;
    }
}

// AccountStore using HashMap
class AccountStore {
    private HashMap<String, Account> accounts = new HashMap<>();
    private ArrayList<String> transactionHistory = new ArrayList<>();

    void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    Collection<Account> getAll() {
        return accounts.values();
    }

    // Sort by balance descending using Comparator
    ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    void addTransaction(String entry) {
        transactionHistory.add(entry);
    }

    ArrayList<String> getTransactionHistory() { return transactionHistory; }
}

public class Main {

    public static void main(String[] args) {
        // Part A Q1 - Word frequency
        System.out.println("=== Word Frequency ===");
        String text = "the cat sat on the mat the cat";
        Map<String, Integer> freq = new HashMap<>();
        for (String w : text.split(" ")) freq.merge(w, 1, Integer::sum);
        freq.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        // Part A Q2 - Unique visitors
        System.out.println("\n=== Unique Visitors ===");
        String[] logins = {"alice", "bob", "alice", "carol", "bob", "dave"};
        Set<String> unique = new LinkedHashSet<>(Arrays.asList(logins));
        System.out.println("Unique count: " + unique.size());
        int count = 0;
        for (String name : unique) {
            if (count++ < 3) System.out.println("  " + name);
        }

        // Part B - AccountStore with HashMap and sorting
        System.out.println("\n=== Account Store ===");
        AccountStore store = new AccountStore();
        store.add(new Account("Riya", 5000));
        store.add(new Account("Rahul", 2000));
        store.add(new Account("Sara", 9000));

        store.addTransaction("DEPOSIT AC0001 500");
        store.addTransaction("WITHDRAW AC0002 200");

        System.out.println("Lookup AC0002: " + store.get("AC0002"));

        System.out.println("\nAccounts sorted by balance (desc):");
        for (Account a : store.sortedByBalance()) {
            System.out.println("  " + a);
        }

        // ConcurrentHashMap for thread-safe access
        ConcurrentHashMap<String, Account> concurrentStore = new ConcurrentHashMap<>();
        for (Account a : store.getAll()) concurrentStore.put(a.getAccountNumber(), a);
        System.out.println("\nConcurrent store size: " + concurrentStore.size());
    }
}
