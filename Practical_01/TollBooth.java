import java.util.Scanner;

public class TollBooth {

    record Vehicle(String number, String type) {}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalToll = 0;
        int bikeCount = 0, carCount = 0, truckCount = 0;

        System.out.println("Enter vehicles (number type). Type 'done' to stop:");

        while (true) {
            System.out.print("Vehicle number: ");
            String number = sc.next();
            if (number.equalsIgnoreCase("done")) break;

            System.out.print("Vehicle type (bike/car/truck): ");
            String type = sc.next().toLowerCase();

            Vehicle v = new Vehicle(number, type);
            int toll = switch (v.type()) {
                case "bike"  -> 20;
                case "car"   -> 50;
                case "truck" -> 150;
                default      -> { System.out.println("Unknown type, skipped."); yield 0; }
            };

            totalToll += toll;
            switch (v.type()) {
                case "bike"  -> bikeCount++;
                case "car"   -> carCount++;
                case "truck" -> truckCount++;
            }
            System.out.println("Toll charged: " + toll);
        }

        System.out.println("Total toll: " + totalToll);

        String mostFrequent = "bike";
        int maxCount = bikeCount;
        if (carCount > maxCount)   { mostFrequent = "car";   maxCount = carCount; }
        if (truckCount > maxCount) { mostFrequent = "truck"; }

        System.out.println("Most frequent: " + mostFrequent);
        sc.close();
    }
}
