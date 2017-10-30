package com.login.login.validation;





import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by giuseppe on 23/08/17.
 */


@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {EmailValidator.class})
@Documented
public @interface ValidEmail {
    String message() default "Email non valida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
