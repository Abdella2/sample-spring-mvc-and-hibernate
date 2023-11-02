package com.student.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdConstraintValidator implements ConstraintValidator<Id, String> {

    private String[] idPrefix;

    @Override
    public void initialize(Id id) {
        idPrefix = id.value();
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        // return id != null ? id.startsWith(idPrefix) : true;
        boolean result = false;

        if (id != null) 
        {for (String pref : idPrefix) {
            result = id.startsWith(pref);

            if (result) break;
        }} else {result = true;}

        return result;
    }

}
