package com.clap.model.Validators;

import java.util.List;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.Role;
import com.clap.model.Tag;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagValidator implements Validator{
    private final UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Tag tag = (Tag) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "", "The tag cannot be empty!");
    }
}
