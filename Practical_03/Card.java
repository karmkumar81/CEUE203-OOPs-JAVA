import java.util.Objects;

public class Card {

    private String rank;
    private String suit;

    Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        return this.rank.equals(other.rank) && this.suit.equals(other.suit);
    }

    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
