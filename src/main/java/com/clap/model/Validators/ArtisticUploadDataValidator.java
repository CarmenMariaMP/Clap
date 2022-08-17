package com.clap.model.Validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clap.model.dataModels.ArtisticContentData;
import com.clap.services.ArtisticContentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArtisticUploadDataValidator implements Validator{
    private final ArtisticContentService artisticContentService;
    @Override
    public boolean supports(Class<?> clazz) {
        return ArtisticContentData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ArtisticContentData artisticContentData = (ArtisticContentData) target;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "", "Title cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "", "Title cannot be empty!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "multipartFile", "", "File cannot be empty!");

        if (errors.getFieldErrorCount("title")==0 && artisticContentService.getIdByTitle(artisticContentData.getTitle())!=null){
            errors.rejectValue("title", "", "This title already exists. Please enter a new one");
        }

        if (artisticContentService.getIdByContentUrl(artisticContentData.getContentUrl())!=null){
            errors.rejectValue("contentUrl", "", "There is a file that already exists with this name. Please enter a new one");
        }

        List<String> validFileExtensions = new ArrayList<String>();
        validFileExtensions.add(".png");
        validFileExtensions.add(".PNG");
        validFileExtensions.add(".jpg");
        validFileExtensions.add(".jpeg");
        validFileExtensions.add(".mp4");
        Boolean isValid = false;
        for(int i=0;i<validFileExtensions.size();i++){
            if(artisticContentData.getContentUrl().contains(validFileExtensions.get(i))){
                isValid =true;
                break;
            }
        }
        if(artisticContentData.getContentUrl().contains("invalidFileName")){
            errors.rejectValue("contentUrl", "", "File cannot be empty!");
        }else{
            if(!isValid){
                errors.rejectValue("contentUrl", "", "Extension invalid. The valid extensions are .png, .PNG, .jpeg, .jpg, .mp4");
            }
        }

        if(errors.getFieldErrorCount("multipartFile")==0 && artisticContentData.getMultipartFile().getSize()>20000000){
            errors.rejectValue("multipartFile", "", "The file you try to upload is too big. The maximun size is 10 MB");
        }
    }
}
