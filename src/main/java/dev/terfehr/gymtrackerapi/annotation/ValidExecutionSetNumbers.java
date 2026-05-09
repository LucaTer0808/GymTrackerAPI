package dev.terfehr.gymtrackerapi.annotation;

import dev.terfehr.gymtrackerapi.annotation.validator.ExecutionSetNumbersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExecutionSetNumbersValidator.class)
public @interface ValidExecutionSetNumbers {

    String message() default "Set numbers must be 1..n without gaps or duplicates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}