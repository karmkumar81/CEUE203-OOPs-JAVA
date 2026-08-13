public class Driver {

    public static void main(String[] args) {
        // Part A Q1 - Password Checker
        String[] passwords = {"abc", "Abcd1234!", "Hello1", "Secure@99"};
        System.out.println("--- Password Strength ---");
        for (String pw : passwords) {
            System.out.println(pw + " -> " + PasswordChecker.strength(pw));
        }

        // Part B - Validator
        System.out.println("\n--- Validator ---");
        System.out.println("isValidMobile(9876543210): " + Validator.isValidMobile("9876543210"));
        System.out.println("isValidMobile(12345):       " + Validator.isValidMobile("12345"));
        System.out.println("isValidEmail(abc@xyz.com):  " + Validator.isValidEmail("abc@xyz.com"));
        System.out.println("isValidPan(ABCDE1234F):     " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("isValidIfsc(SBIN0001234):   " + Validator.isValidIfsc("SBIN0001234"));

        // Part B - CommandParser
        System.out.println("\n--- Command Parser ---");
        Command cmd = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("Type: " + cmd.type() + " | Account: " + cmd.accountNumber() + " | Amount: " + cmd.amount());

        // Part B - StatementFormatter
        Account acc = new Account("Riya", 5000);
        acc.deposit(2000);
        acc.withdraw(1000);
        System.out.println("\n--- Statement ---");
        System.out.println(StatementFormatter.buildStatement(acc));
    }
}
