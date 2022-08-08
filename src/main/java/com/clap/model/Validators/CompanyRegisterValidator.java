package com.clap.model.Validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.dataModels.CompanyRegisterData;

@Component
public class CompanyRegisterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyRegisterData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyRegisterData companyRegisterData = (CompanyRegisterData) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "", "Username cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "", "Email cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "", "Phone cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "", "Password cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "",
				"Password confirmation cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName", "", "Company name cannot be empty!");

        if (errors.getFieldErrorCount("password") == 0 && errors.getFieldErrorCount("confirmPassword") == 0
                && !companyRegisterData.getPassword().equals(companyRegisterData.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "", "Passwords don't match!");
        }

        if (errors.getFieldErrorCount("password") == 0 && companyRegisterData.getPassword().length() < 8 || companyRegisterData.getPassword().length() > 64) {
			errors.rejectValue("password", "", "Password length is out of bounds! (between 8 and 64 characters)");
		}

        if (errors.getFieldErrorCount("username") == 0 && companyRegisterData.getUsername().length() < 4 || companyRegisterData.getUsername().length() > 20) {
			errors.rejectValue("username", "", "Username length is out of bounds! (between 4 and 20 characters)");
		}

        if (errors.getFieldErrorCount("companyName") == 0 && companyRegisterData.getCompanyName().length() < 4 || companyRegisterData.getCompanyName().length() > 64) {
			errors.rejectValue("companyName", "", "Company name length is out of bounds! (between 4 and 64 characters)");
		}

        if (errors.getFieldErrorCount("email") == 0 && !patternMatches(companyRegisterData.getEmail(), "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
            errors.rejectValue("email", "", "The email entered is invalid");
        }

        if (errors.getFieldErrorCount("phone") == 0 && !patternMatches(companyRegisterData.getPhone(),"^((\\+)?[1-9]{1,2})?([-\\s\\.])?((\\(\\d{1,4}\\))|\\d{1,4})(([-\\s\\.])?[0-9]{1,12}){1,2}$")){
            errors.rejectValue("phone", "", "The phone entered is invalid");
        }

        if (errors.getFieldErrorCount("taxIdNumber") == 0 && !patternMatches(companyRegisterData.getTaxIdNumber(),"^([0-9]{9})$")){
            errors.rejectValue("taxIdNumber", "", "The tax Id number entered is invalid, it must have 9 numbers");
        }
        
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
    }

}
