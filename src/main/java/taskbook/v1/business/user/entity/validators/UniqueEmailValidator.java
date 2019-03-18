package taskbook.v1.business.user.entity.validators;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import taskbook.v1.business.user.control.UserStore;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
	
	
	private UserStore persistence;
	
	@Override
	public void initialize(UniqueEmail constraintAnnotation) {
		try {
            javax.naming.Context context = new InitialContext();
            this.persistence = (UserStore) context.lookup("java:global/taskbook/UserStoreImpl!taskbook.v1.business.user.control.UserStore");
        } catch (NamingException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return persistence
					.existsEmail(value) == null;
	}
}
