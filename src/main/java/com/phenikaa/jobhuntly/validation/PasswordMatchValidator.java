package com.phenikaa.jobhuntly.validation;

import com.phenikaa.jobhuntly.auth.AuthDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, AuthDTO.ResetPasswordRequest> {

    @Override
    public boolean isValid(AuthDTO.ResetPasswordRequest resetPasswordRequest, ConstraintValidatorContext constraintValidatorContext) {
        return resetPasswordRequest.password().equals(resetPasswordRequest.passwordConfirmation());
    }

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
