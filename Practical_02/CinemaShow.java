public class CinemaShow {

    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    // Constructor chaining
    CinemaShow(String title) {
        this(title, 100);
    }

    boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        }
        return false;
    }

    void cancel(int n) {
        seatsAvailable = Math.min(seatsAvailable + n, capacity);
    }

    int getSeatsAvailable() { return seatsAvailable; }

    static int getTotalBooked() { return totalBooked; }

    public static void main(String[] args) {
        CinemaShow show = new CinemaShow("Avengers", 10);

        System.out.println("Booking 4 seats: " + show.book(4) + " | Available: " + show.getSeatsAvailable());
        System.out.println("Booking 3 seats: " + show.book(3) + " | Available: " + show.getSeatsAvailable());
        System.out.println("Booking 5 seats: " + show.book(5) + " | Available: " + show.getSeatsAvailable());  // should fail
        show.cancel(2);
        System.out.println("Cancelled 2 seats | Available: " + show.getSeatsAvailable());
        System.out.println("Total booked: " + CinemaShow.getTotalBooked());
    }
}
