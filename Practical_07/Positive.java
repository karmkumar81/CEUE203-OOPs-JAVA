import java.lang.annotation.*;

// Custom annotation @Positive - validates that a numeric field is > 0
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Positive {
    String message() default "must be > 0";
}
