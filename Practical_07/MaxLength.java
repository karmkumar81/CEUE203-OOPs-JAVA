import java.lang.annotation.*;

// Custom annotation @MaxLength - validates max string length
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength {
    int value();
}
