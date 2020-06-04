package ru.megafon.request.handler.validator;

import ru.megafon.request.handler.annotation.PasswordMatches;
import ru.megafon.request.handler.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDto> {

    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
