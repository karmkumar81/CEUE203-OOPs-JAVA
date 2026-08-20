public class Main {

    public static void main(String[] args) {
        // Part A - Shape areas (quick demo)
        System.out.println("--- Part A: Shapes ---");
        // Circle area: pi*r^2, Rectangle: w*h, Triangle: 0.5*b*h
        double[] areas = { Math.PI * 5 * 5, 4.0 * 6, 0.5 * 3 * 8 };
        String[] shapes = { "Circle(r=5)", "Rectangle(4x6)", "Triangle(b=3,h=8)" };
        double total = 0;
        double max = areas[0];
        String maxShape = shapes[0];
        for (int i = 0; i < shapes.length; i++) {
            System.out.printf("%s area = %.2f%n", shapes[i], areas[i]);
            total += areas[i];
            if (areas[i] > max) { max = areas[i]; maxShape = shapes[i]; }
        }
        System.out.printf("Total area = %.2f | Largest: %s%n", total, maxShape);

        // Part B - Account polymorphism
        System.out.println("\n--- Part B: Account Types ---");
        Account[] accounts = {
            new SavingsAccount("Riya", 10000, 1000),
            new CurrentAccount("Rahul", 5000, 2000),
            new FixedDepositAccount("Sara", 50000)
        };

        for (Account a : accounts) {
            System.out.println(a);
            System.out.println("  Interest rate: " + a.interestRate() + "%");

            // Pattern instanceof for special handling
            if (a instanceof FixedDepositAccount fd) {
                System.out.println("  Note: This is a Fixed Deposit — locked for term.");
            }
        }
    }
}
