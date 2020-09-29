package com.domgarr.RedditClone.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
@AllArgsConstructor
public class ValidationService {
    private final Validator validator;

    public <T> String validate(T object){
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if(violations.size() > 0){
            for(ConstraintViolation<T> violation : violations){
                sb.append(generateErrorString(violation));
            }
        }
        return sb.toString();
    }

    public <T> String validateProperty(T object, String property){
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<T>> violations = validator.validateProperty(object, property);

        for(ConstraintViolation<T> violation : violations){
            sb.append(generateErrorString(violation));
        }
        return sb.toString();
    }

    private String generatePasswordStars(int size){
        StringBuilder sb =  new StringBuilder();
        for(int i = 0; i < size; i++){
            sb.append("*");
        }
        return sb.toString();
    }

    private <T> String generateErrorString(ConstraintViolation<T> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        String message = violation.getMessage();

        String invalidValue =  violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : "";

        String errorMessage;

        if(violation.getPropertyPath().toString().equals("password")){
            errorMessage = propertyPath + ": " + message + " (value: " + generatePasswordStars( invalidValue.length()) + ")\n";
        }else{
            errorMessage = propertyPath + ": " + message + " (value: " + invalidValue + ")\n";
        }

        return errorMessage;
    }

}
