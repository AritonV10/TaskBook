package taskbook.v1.business.task.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import taskbook.v1.platform.utility.BinaryTree;
import taskbook.v1.platform.utility.UniqueBinaryTree;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Constraint(validatedBy = { ValidEnum.ValidEnumValidator.class })
public @interface ValidEnum {
	
	public abstract String message();
    public abstract Class<?>[] groups()  default {};
    public abstract Class<? extends Payload>[] payload() default {};
    public abstract Class<? extends java.lang.Enum<?>> enumClass();
    
    public class ValidEnumValidator implements ConstraintValidator<ValidEnum, Object> {

    	private BinaryTree<String> values;
    	
    	public void setValues(Class<? extends Enum<?>> targetEnum) {
    		Object[] possibleValues = targetEnum.getEnumConstants();
    		this.values = new UniqueBinaryTree<>(possibleValues[0].toString());
    		for(Object ob : possibleValues) {
    			values.add(ob.toString());
    		}
    	}
    	
    	@Override
    	public void initialize(ValidEnum constraintAnnotation) {
    		setValues(constraintAnnotation.enumClass());
    	}
    	
		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if(value != null && !value.toString().isEmpty()) {
				String actualValue = value.toString();
				if(values.get(actualValue) != null) {
					return true;
				}
			}
			return false;
		}
    	
    }
}
