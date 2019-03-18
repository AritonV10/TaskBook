package taskbook.v1.platform.annotations;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Target;

/**
 * 
 * @author vio
 * Used in a concurrent context
 * Says who protects the entity that this annotation is on
 */
@Target(FIELD)
public @interface GuardedBy {

	String guardian();
}
