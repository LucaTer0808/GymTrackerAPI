package dev.terfehr.gymtrackerapi.annotation.validator;

import dev.terfehr.gymtrackerapi.annotation.Password;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        return value.matches(User.PASSWORD_REGEX);
    }

}
