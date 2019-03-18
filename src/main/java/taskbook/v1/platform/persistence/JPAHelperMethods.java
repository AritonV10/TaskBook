package taskbook.v1.platform.persistence;

import java.util.List;

import javax.persistence.Query;

public interface JPAHelperMethods {
	
	/**
	 * JPA {@code getSingleResult} throws an exception if there are no entities found
	 * but {@code getResultList} doesn't
	 * @param query
	 * @return the generic entity
	 */
	default <T> T getSingleResult(Query query) {
		List<T> result = query.getResultList();
		if(result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
}
