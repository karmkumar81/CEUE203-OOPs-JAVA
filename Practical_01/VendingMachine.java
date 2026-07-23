import java.util.Scanner;

public class VendingMachine {

    enum Coin { ONE, TWO, FIVE, TEN }

    public static void main(String[] args) {
        int price = 15;
        int total = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("Snack price: " + price);
        System.out.println("Insert coins (ONE, TWO, FIVE, TEN):");

        while (total < price) {
            String input = sc.next().toUpperCase();
            try {
                Coin coin = Coin.valueOf(input);
                int value = switch (coin) {
                    case ONE  -> 1;
                    case TWO  -> 2;
                    case FIVE -> 5;
                    case TEN  -> 10;
                };
                total += value;
                System.out.println("Total so far: " + total);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid coin. Use: ONE, TWO, FIVE, TEN");
            }
        }

        System.out.println("Paid. Change: " + (total - price));
        sc.close();
    }
}
