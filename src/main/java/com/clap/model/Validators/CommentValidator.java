package com.clap.model.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.Comment;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return Comment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "", "The comment cannot be empty!");
    }
}
