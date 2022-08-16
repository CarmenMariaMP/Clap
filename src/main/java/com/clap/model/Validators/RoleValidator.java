package com.clap.model.Validators;

import java.util.List;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.Role;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleValidator implements Validator{
    private final UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Role.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Role role = (Role) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "", "Username cannot be empty!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "", "The role cannot be empty!");

        List<String> validUsernames = userService.getAllUsernames();
        Boolean isValid = false;
        for(int i=0;i<validUsernames.size();i++){
            if(role.getUsername().contains(validUsernames.get(i))){
                isValid =true;
                break;
            }
        }

        if(!isValid){
            errors.rejectValue("username", "", "There is no user with this username");
        }
    }
}
