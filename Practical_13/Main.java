import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

// Practical 13: Generic Repository using @Id and reflection

// @Id annotation (from Practical 7)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Id {}

// Account with @Id annotation
class Account {
    @Id
    private String accountNumber;
    private String ownerName;
    private long balance;
    private static long counter = 0;

    Account(String ownerName, long balance) {
        this.accountNumber = String.format("AC%04d", ++counter);
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    String getAccountNumber() { return accountNumber; }
}

// Generic Repository<T> using @Id for key lookup
class Repository<T> {

    private HashMap<String, T> store = new HashMap<>();

    // Save entity - uses reflection to read @Id field
    T save(T entity) {
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    String id = (String) field.get(entity);
                    store.put(id, entity);
                    return entity;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot read @Id field", e);
        }
        throw new RuntimeException("No @Id field found in " + entity.getClass().getName());
    }

    // Save multiple entities at once
    @SafeVarargs
    final void saveAll(T... items) {
        for (T item : items) save(item);
    }

    T findById(String id)     { return store.get(id); }
    boolean delete(String id) { return store.remove(id) != null; }
    ArrayList<T> findAll()    { return new ArrayList<>(store.values()); }

    // Generic bounded method: average of any Number array
    static <N extends Number> double average(N[] values) {
        double sum = 0;
        for (N v : values) sum += v.doubleValue();
        return values.length == 0 ? 0 : sum / values.length;
    }
}

public class Main {

    public static void main(String[] args) {
        // Part A - Generic containers demo
        System.out.println("=== Generic Box ===");
        String[] words = {"hello", "world"};
        Integer[] nums = {10, 20, 30};
        System.out.println("Average of ints:    " + Repository.average(nums));
        System.out.println("Average of doubles: " + Repository.average(new Double[]{1.5, 2.5, 3.0}));

        // Part B - Generic Repository<Account>
        System.out.println("\n=== Repository<Account> ===");
        Repository<Account> repo = new Repository<>();

        Account a1 = new Account("Riya", 5000);
        Account a2 = new Account("Rahul", 3000);
        Account a3 = new Account("Sara", 8000);

        repo.saveAll(a1, a2, a3);

        System.out.println("findById(AC0002): " + repo.findById("AC0002"));

        System.out.println("\nAll accounts:");
        for (Account a : repo.findAll()) System.out.println("  " + a);

        System.out.println("\nDelete AC0001: " + repo.delete("AC0001"));
        System.out.println("After delete, size: " + repo.findAll().size());
    }
}
