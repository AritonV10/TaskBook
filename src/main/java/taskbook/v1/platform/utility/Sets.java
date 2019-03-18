package taskbook.v1.platform.utility;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author vio
 * Utility methods used with {@link Set}
 */
public final class Sets {
	
	@SafeVarargs
	public static <T> Set<T> asSet(T ... args) {
		return Arrays
				.stream(args)
				.collect(Collectors.toSet());
	}
}
