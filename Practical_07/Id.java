import java.lang.annotation.*;

// Custom annotation @Id - marks the primary key field
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {}
