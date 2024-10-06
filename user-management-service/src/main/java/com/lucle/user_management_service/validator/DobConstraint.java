package com.lucle.user_management_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD }) // annotation se duoc apply o dau
@Retention(RetentionPolicy.RUNTIME) // duoc xu ly khi nao:
@Constraint(validatedBy = { DobValidator.class })
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int min();
}
