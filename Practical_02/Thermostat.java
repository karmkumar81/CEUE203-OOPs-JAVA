public class Thermostat {

    private String location;
    private int temperature;
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;

    // Constructor with location and starting temperature
    Thermostat(String location, int startTemp) {
        this.location = location;
        this.temperature = (startTemp >= MIN && startTemp <= MAX) ? startTemp : 22;
        activeCount++;
    }

    // Constructor chaining
    Thermostat(String location) {
        this(location, 22);
    }

    void raise() {
        if (temperature < MAX) temperature++;
        else System.out.println("Already at maximum (30)");
    }

    void lower() {
        if (temperature > MIN) temperature--;
        else System.out.println("Already at minimum (16)");
    }

    int getTemperature() { return temperature; }

    static int getActiveCount() { return activeCount; }

    public static void main(String[] args) {
        Thermostat t1 = new Thermostat("Living Room", 22);
        Thermostat t2 = new Thermostat("Bedroom");

        System.out.println("Raising t1 temperature 10 times:");
        for (int i = 0; i < 10; i++) {
            t1.raise();
            System.out.println("t1 temp: " + t1.getTemperature());
        }

        System.out.println("\nLowering t1 temperature 20 times:");
        for (int i = 0; i < 20; i++) {
            t1.lower();
            System.out.println("t1 temp: " + t1.getTemperature());
        }

        System.out.println("\nActive thermostat count: " + Thermostat.getActiveCount());
    }
}
