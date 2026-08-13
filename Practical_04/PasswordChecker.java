public class PasswordChecker {

    static String strength(String pw) {
        int score = 0;
        if (pw.length() >= 8) score++;
        if (pw.matches(".*[A-Z].*")) score++;
        if (pw.matches(".*[0-9].*")) score++;
        if (pw.matches(".*[!@#$%^&*()].*")) score++;

        if (score <= 1) return "Weak";
        if (score <= 3) return "Medium";
        return "Strong";
    }
}
