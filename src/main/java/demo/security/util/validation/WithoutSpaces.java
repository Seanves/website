package demo.security.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WithoutSpacesValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithoutSpaces {
    String message() default "Has space";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
