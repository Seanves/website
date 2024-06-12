package demo.security.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements
                            ConstraintValidator<StrongPassword, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!@#$%^&*_-]{8,20}$");
    }
}
