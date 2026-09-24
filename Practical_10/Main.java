import java.util.concurrent.*;

// Practical 10: Thread pool, producer-consumer, deadlock and fix

class Account {
    private String accountNumber;
    private long balance;

    Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    synchronized void deposit(long amount) { balance += amount; }

    synchronized void withdraw(long amount) {
        if (balance >= amount) balance -= amount;
    }

    String getAccountNumber() { return accountNumber; }
    long getBalance()         { return balance; }
}

// TransactionProcessor using fixed thread pool
class TransactionProcessor {
    private ExecutorService pool = Executors.newFixedThreadPool(4);

    void submit(Runnable task) { pool.execute(task); }

    void stop() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }
}

// Simple shared buffer for producer-consumer
class SharedBuffer {
    private int[] buffer = new int[5];
    private int count = 0;

    synchronized void produce(int item) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[count++] = item;
        System.out.println("Produced: " + item);
        notifyAll();
    }

    synchronized int consume() throws InterruptedException {
        while (count == 0) wait();
        int item = buffer[--count];
        System.out.println("Consumed: " + item);
        notifyAll();
        return item;
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // === Part 1: Thread Pool ===
        System.out.println("=== Thread Pool Demo ===");
        TransactionProcessor processor = new TransactionProcessor();
        Account acc = new Account("AC0001", 5000);

        for (int i = 1; i <= 6; i++) {
            final int id = i;
            processor.submit(() -> {
                acc.deposit(100);
                System.out.println("Task " + id + " deposited 100 | Thread: " + Thread.currentThread().getName());
            });
        }
        processor.stop();
        System.out.println("Final balance after pool: " + acc.getBalance());

        // === Part 2: Producer-Consumer ===
        System.out.println("\n=== Producer-Consumer Demo ===");
        SharedBuffer buf = new SharedBuffer();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) buf.produce(i);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) buf.consume();
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // === Part 3: Deadlock Fix (lock by accountNumber order) ===
        System.out.println("\n=== Deadlock Fix Demo ===");
        Account a = new Account("AC0001", 10000);
        Account b = new Account("AC0002", 10000);

        // Always lock the account with smaller number first to avoid deadlock
        Thread t1 = new Thread(() -> safeTransfer(a, b, 500));
        Thread t2 = new Thread(() -> safeTransfer(b, a, 300));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("A balance: " + a.getBalance());
        System.out.println("B balance: " + b.getBalance());
    }

    // Transfer with consistent lock ordering to prevent deadlock
    static void safeTransfer(Account from, Account to, long amount) {
        Account first  = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
                System.out.println(Thread.currentThread().getName()
                    + " transferred " + amount + " from " + from.getAccountNumber()
                    + " to " + to.getAccountNumber());
            }
        }
    }
}
