package taskbook.v1.business.group.entity.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import taskbook.v1.business.group.control.GroupStore;
import taskbook.v1.platform.CDI.BeanService;

public class UniqueGroupNameValidator implements ConstraintValidator<UniqueGroupName, String>{

	private GroupStore persistence;
	
	@Override
	public void initialize(UniqueGroupName constraintAnnotation) {
		persistence = BeanService.getInstance().getCDIBean(GroupStore.class);
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return
				persistence
				.existsGroup(value);
	}

}
