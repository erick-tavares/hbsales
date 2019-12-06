package br.com.hbsis.fornecedor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CnpjValidator.class)
@Documented
public @interface CnpjValidation {

	String message() default "";

	long length() default -1L;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

