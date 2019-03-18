package taskbook.v1.business.group.entity.validators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = UniqueGroupNameValidator.class)
public @interface UniqueGroupName {
	String message();
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
