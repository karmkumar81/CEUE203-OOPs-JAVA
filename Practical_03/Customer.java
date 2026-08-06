public class Customer implements Cloneable {

    private final String customerId;
    private String name;
    private String email;
    private String mobile;
    private Address address;
    private static long customerCounter = 100;

    // Static nested class
    public static class Address {
        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine()    { return line; }
        public String getCity()    { return city; }
        public String getPincode() { return pincode; }
    }

    private static String generateCustomerId() {
        return "CUST" + (++customerCounter);
    }

    Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    void setAddress(Address address) { this.address = address; }

    // Clone method
    public Customer clone() throws CloneNotSupportedException {
        return (Customer) super.clone();
    }

    String getCustomerId() { return customerId; }
    String getName()       { return name; }
    String getEmail()      { return email; }
    String getMobile()     { return mobile; }
    Address getAddress()   { return address; }
}
