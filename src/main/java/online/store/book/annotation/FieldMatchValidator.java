package online.store.book.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field firstField = obj.getClass().getDeclaredField(firstFieldName);
            Field secondField = obj.getClass().getDeclaredField(secondFieldName);
            firstField.setAccessible(true);
            secondField.setAccessible(true);
            Object firstObj = firstField.get(obj);
            Object secondObj = secondField.get(obj);
            return firstObj != null && firstObj.equals(secondObj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
