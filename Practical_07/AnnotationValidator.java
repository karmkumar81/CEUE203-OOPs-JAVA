import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                // Check @Positive annotation
                if (field.isAnnotationPresent(Positive.class)) {
                    Positive ann = field.getAnnotation(Positive.class);
                    if (value instanceof Number num && num.longValue() <= 0) {
                        errors.add("Field '" + field.getName() + "': " + ann.message());
                    }
                }

                // Check @MaxLength annotation
                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength ann = field.getAnnotation(MaxLength.class);
                    if (value instanceof String str && str.length() > ann.value()) {
                        errors.add("Field '" + field.getName() + "': exceeds max length " + ann.value());
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Cannot access field: " + field.getName());
            }
        }

        return errors.toArray(new String[0]);
    }
}
