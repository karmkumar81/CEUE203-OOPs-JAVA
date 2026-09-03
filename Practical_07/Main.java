public class Main {

    public static void main(String[] args) {
        // Test with invalid account (negative balance)
        Account invalidAccount = new Account("Riya", -100);
        System.out.println("Validating account with balance -100:");
        String[] errors = AnnotationValidator.validate(invalidAccount);

        if (errors.length == 0) {
            System.out.println("No errors found.");
        } else {
            for (String err : errors) {
                System.out.println("ERROR: " + err);
            }
        }

        // Test with valid account
        Account validAccount = new Account("Rahul", 5000);
        System.out.println("\nValidating account with balance 5000:");
        String[] errors2 = AnnotationValidator.validate(validAccount);

        if (errors2.length == 0) {
            System.out.println("No errors found.");
        } else {
            for (String err : errors2) {
                System.out.println("ERROR: " + err);
            }
        }
    }
}
