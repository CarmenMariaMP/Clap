package com.clap.model.Validators;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.dataModels.ContentCreatorManagementData;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContentCreatorManagementValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ContentCreatorManagementData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContentCreatorManagementData contentCreatorManagementData = (ContentCreatorManagementData) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "", "Username cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "", "Email cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "", "Full name cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "", "Phone cannot be empty!");

        if (errors.getFieldErrorCount("username") == 0 && contentCreatorManagementData.getUsername().length() < 4
                || contentCreatorManagementData.getUsername().length() > 20) {
            errors.rejectValue("username", "", "Username length is out of bounds! (between 4 and 20 characters)");
        }

        if (errors.getFieldErrorCount("fullName") == 0 && contentCreatorManagementData.getFullName().length() < 4
                || contentCreatorManagementData.getFullName().length() > 64) {
            errors.rejectValue("fullName", "", "Full name length is out of bounds! (between 4 and 64 characters)");
        }

        if (errors.getFieldErrorCount("email") == 0 && !patternMatches(contentCreatorManagementData.getEmail(),
                "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            errors.rejectValue("email", "", "The email entered is invalid");
        }

        if (errors.getFieldErrorCount("phone") == 0 && !patternMatches(contentCreatorManagementData.getPhone(),
                "^((\\+)?[1-9]{1,2})?([-\\s\\.])?((\\(\\d{1,4}\\))|\\d{1,4})(([-\\s\\.])?[0-9]{1,12}){1,2}$")) {
            errors.rejectValue("phone", "", "The phone entered is invalid");
        }

        List<String> usernames = userService.getAllUsernames();
        String username_logged_user = userService.getLoggedUser();
        for (int i = 0; i < usernames.size(); i++) {
            if (errors.getFieldErrorCount("username") == 0
                    && !contentCreatorManagementData.getUsername().equals(username_logged_user)) {
                if (usernames.get(i).equals(contentCreatorManagementData.getUsername())) {
                    errors.rejectValue("username", "", "This username is taken. Try another one");
                }
            }
        }
        String png = ".png";
        String PNG = ".PNG";
        String jpg = ".jpg";
        String jpeg = ".jpeg";
        if (!contentCreatorManagementData.getPhotoUrl().contains(png)
                && !contentCreatorManagementData.getPhotoUrl().contains(PNG)
                && !contentCreatorManagementData.getPhotoUrl().contains(jpg)
                && !contentCreatorManagementData.getPhotoUrl().contains(jpeg)) {
            errors.rejectValue("photoUrl", "", "Extension invalid. The valid extensions are .png, .PNG, .jpeg, .jpg");
        }
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
