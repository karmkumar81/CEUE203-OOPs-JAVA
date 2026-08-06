public class Driver {

    public static void main(String[] args) {
        // Part A Q1 - Distinct points
        Point[] points = {
            new Point(1, 2), new Point(3, 4), new Point(1, 2),
            new Point(5, 6), new Point(3, 4)
        };

        int distinct = 0;
        for (int i = 0; i < points.length; i++) {
            boolean found = false;
            for (int j = 0; j < i; j++) {
                if (points[i].equals(points[j])) { found = true; break; }
            }
            if (!found) distinct++;
        }
        System.out.println("Distinct points: " + distinct);

        // Part A Q2 - Duplicate card check
        Card[] cards = {
            new Card("Ace", "Spades"), new Card("Queen", "Hearts"),
            new Card("King", "Clubs"), new Card("Ace", "Spades")
        };

        outer:
        for (int i = 1; i < cards.length; i++) {
            for (int j = 0; j < i; j++) {
                if (cards[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + cards[i]);
                    break outer;
                }
            }
        }

        // Part B - Account toString, equals, instanceof
        System.out.println("\n--- Part B: Account ---");
        Account a1 = new Account("Riya", 4000);
        Account a2 = new Account("Riya", 4000);  // same number? No — different objects

        System.out.println(a1);
        System.out.println("a1.equals(a2): " + a1.equals(a2));  // false (different account numbers)

        // instanceof check
        Object obj = a1;
        System.out.println("obj instanceof Account: " + (obj instanceof Account));

        // Customer with Address
        Customer c = new Customer("Riya", "riya@email.com", "9876543210");
        Customer.Address addr = new Customer.Address("123 MG Road", "Surat", "395001");
        c.setAddress(addr);
        System.out.println("Customer: " + c.getName() + " | City: " + c.getAddress().getCity());

        // Clone
        try {
            Customer c2 = c.clone();
            System.out.println("Cloned customer: " + c2.getName());
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone failed: " + e.getMessage());
        }
    }
}
