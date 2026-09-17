// Practical 9: Threads, race condition, and synchronization

class Account {
    private long balance;

    Account(long initialBalance) { this.balance = initialBalance; }

    // Without synchronization (causes race condition)
    void depositUnsafe(long amount) {
        long cur = balance;
        Thread.yield();
        balance = cur + amount;
    }

    // With synchronization (thread-safe)
    synchronized void deposit(long amount) {
        balance += amount;
    }

    long getBalance() { return balance; }
    void setBalance(long balance) { this.balance = balance; }
}

// Runnable worker that deposits into an account
class AccountWorker implements Runnable {
    private Account account;
    private int times;
    private long amount;

    AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    public void run() {
        for (int i = 0; i < times; i++) {
            account.deposit(amount);
        }
        System.out.println(Thread.currentThread().getName() + " finished.");
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 10;
        int depositsPerThread = 1000;
        long depositAmount = 1;
        long expected = numThreads * depositsPerThread * depositAmount;

        // === Without synchronization (race condition demo) ===
        Account unsafeAccount = new Account(0);
        Thread[] unsafeThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < depositsPerThread; j++) {
                    unsafeAccount.depositUnsafe(depositAmount);
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) t.join();
        System.out.println("Expected balance: " + expected);
        System.out.println("Without sync (wrong): " + unsafeAccount.getBalance());

        // === With synchronization (correct) ===
        Account safeAccount = new Account(0);
        Thread[] safeThreads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            safeThreads[i] = new Thread(new AccountWorker(safeAccount, depositsPerThread, depositAmount));
            safeThreads[i].setName("Worker-" + i);
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) t.join();
        System.out.println("\nWith sync (correct): " + safeAccount.getBalance());
        System.out.println("Test passed: " + (safeAccount.getBalance() == expected));
    }
}
