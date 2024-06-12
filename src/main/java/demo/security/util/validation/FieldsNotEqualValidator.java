package demo.security.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldsNotEqualValidator
        implements ConstraintValidator<FieldsNotEqual, Object> {

    private String field;
    private String fieldMatch;

    public void initialize(FieldsNotEqual constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.otherField();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        boolean isValid =  fieldValue != null && !fieldValue.equals(fieldMatchValue);

        if (!isValid) {
            context.buildConstraintViolationWithTemplate(
                context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(field)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
