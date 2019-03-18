package taskbook.v1.platform.utility;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author vio
 * Used with {@link JSON} to specify which fields not to serialize
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface JSONSkip {

}
