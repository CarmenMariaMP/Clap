package com.clap.model.Validators;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.dataModels.CompanyManagementData;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyManagementValidator implements Validator {

    private final UserService userService;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyManagementData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyManagementData companyManagementData = (CompanyManagementData) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "", "Username cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "", "Email cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "", "Phone cannot be empty!");

        if (errors.getFieldErrorCount("username") == 0 && companyManagementData.getUsername().length() < 4 || companyManagementData.getUsername().length() > 20) {
			errors.rejectValue("username", "", "Username length is out of bounds! (between 4 and 20 characters)");
		}

        if (errors.getFieldErrorCount("companyName") == 0 && companyManagementData.getCompanyName().length() < 4 || companyManagementData.getCompanyName().length() > 64) {
			errors.rejectValue("companyName", "", "Company name length is out of bounds! (between 4 and 64 characters)");
		}

        if (errors.getFieldErrorCount("email") == 0 && !patternMatches(companyManagementData.getEmail(), "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
            errors.rejectValue("email", "", "The email entered is invalid");
        }

        if (errors.getFieldErrorCount("phone") == 0 && !patternMatches(companyManagementData.getPhone(),"^((\\+)?[1-9]{1,2})?([-\\s\\.])?((\\(\\d{1,4}\\))|\\d{1,4})(([-\\s\\.])?[0-9]{1,12}){1,2}$")){
            errors.rejectValue("phone", "", "The phone entered is invalid");
        }

        if (errors.getFieldErrorCount("taxIdNumber") == 0 && !patternMatches(companyManagementData.getTaxIdNumber(),"^([0-9]{9})$")){
            errors.rejectValue("taxIdNumber", "", "The tax Id number entered is invalid, it must have 9 numbers");
        }

        List<String> usernames= userService.getAllUsernames();
        String username_logged_user = userService.getLoggedUser();
        for (int i=0;i<usernames.size();i++){
            if(errors.getFieldErrorCount("username") == 0 && !companyManagementData.getUsername().equals(username_logged_user)){
                if(usernames.get(i).equals(companyManagementData.getUsername())){
                    errors.rejectValue("username", "", "This username is taken. Try another one");
                }
            }
        }
        
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
    }

}
