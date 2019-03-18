package taskbook.v1.business.user.entity.validators;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(FIELD)
@Constraint(validatedBy = { UniqueEmailValidator.class })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
	
	String message();
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
