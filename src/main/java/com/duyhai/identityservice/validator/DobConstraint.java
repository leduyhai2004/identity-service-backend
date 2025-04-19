package com.duyhai.identityservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.FIELD}) // trong 1 field trong 1 obect/class
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "invalid date of birth";

    int min(); // khai bao them de validate(customize)

    Class<?>[] groups() default {};//field cần có

    Class<? extends Payload>[] payload() default {};//field cần có
}
