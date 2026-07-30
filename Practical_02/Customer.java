public class Customer {

    private final String customerId;
    private String name;
    private String email;
    private String mobile;
    private static long customerCounter = 100;

    private static String generateCustomerId() {
        return "CUST" + (++customerCounter);
    }

    Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    String getCustomerId() { return customerId; }
    String getName()       { return name; }
    String getEmail()      { return email; }
    String getMobile()     { return mobile; }
}
