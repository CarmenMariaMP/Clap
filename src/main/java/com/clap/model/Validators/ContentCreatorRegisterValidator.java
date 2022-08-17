package com.clap.model.Validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.dataModels.ContentCreatorRegisterData;

@Component
public class ContentCreatorRegisterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ContentCreatorRegisterData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContentCreatorRegisterData contentCreatorRegisterData = (ContentCreatorRegisterData) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "", "Username cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "", "Full name cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "", "Email cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "", "Password cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "",
				"Password confirmation cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "", "Full name cannot be empty!");

        if (errors.getFieldErrorCount("password") == 0 && errors.getFieldErrorCount("confirmPassword") == 0
                && !contentCreatorRegisterData.getPassword().equals(contentCreatorRegisterData.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "", "Passwords don't match!");
        }

        if (errors.getFieldErrorCount("password") == 0 && contentCreatorRegisterData.getPassword().length() < 8 || contentCreatorRegisterData.getPassword().length() > 64) {
			errors.rejectValue("password", "", "Password length is out of bounds! (between 8 and 64 characters)");
		}

        if (errors.getFieldErrorCount("username") == 0 && contentCreatorRegisterData.getUsername().length() < 4 || contentCreatorRegisterData.getUsername().length() > 20) {
			errors.rejectValue("username", "", "Username length is out of bounds! (between 4 and 20 characters)");
		}

        if (errors.getFieldErrorCount("fullName") == 0 && contentCreatorRegisterData.getFullName().length() < 4 || contentCreatorRegisterData.getFullName().length() > 64) {
			errors.rejectValue("fullName", "", "Full name length is out of bounds! (between 4 and 64 characters)");
		}

        if (errors.getFieldErrorCount("email") == 0 && !patternMatches(contentCreatorRegisterData.getEmail(), "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
            errors.rejectValue("email", "", "The email entered is invalid");
        }

        if (!contentCreatorRegisterData.getPhone().isEmpty() && !patternMatches(contentCreatorRegisterData.getPhone(),"^((\\+)?[1-9]{1,2})?([-\\s\\.])?((\\(\\d{1,4}\\))|\\d{1,4})(([-\\s\\.])?[0-9]{1,12}){1,2}$")){
            errors.rejectValue("phone", "", "The phone entered is invalid");
        }
        
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
    }

}
