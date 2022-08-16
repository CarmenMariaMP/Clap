package com.clap.model.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.Tag;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "", "The tag cannot be empty!");
    }
}
