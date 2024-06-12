package demo.security.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WithoutSpacesValidator implements
                    ConstraintValidator<WithoutSpaces, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext cvc) {
        return !s.contains(" ");
    }
}
