package com.phenikaa.jobhuntly.validation;

import com.phenikaa.jobhuntly.auth.AuthDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchRegisterValidator implements ConstraintValidator<PasswordMatch, AuthDTO.RegisterRequest> {

    @Override
    public boolean isValid(AuthDTO.RegisterRequest registerRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (!registerRequest.password().equals(registerRequest.passwordConfirmation())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("passwordConfirmation")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}