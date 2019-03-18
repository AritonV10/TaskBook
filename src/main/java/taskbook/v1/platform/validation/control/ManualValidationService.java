package taskbook.v1.platform.validation.control;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ManualValidationService {
	
	public <T> void validateBeanProperty(T entity, final String propertyName) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validateProperty(entity, propertyName);
		if(!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
	
	
}
