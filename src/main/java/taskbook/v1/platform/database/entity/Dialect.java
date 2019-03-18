package taskbook.v1.platform.database.entity;

import org.hibernate.Hibernate;

/**
 * 
 * @author vio
 * Dialects for {@link Hibernate}
 */
public enum Dialect {
	MySQL("org.hibernate.dialect.MySQL57Dialect"),
	H2("org.hibernate.dialect.H2Dialect");
	
	private final String toString;
	Dialect(final String toString) {
		this.toString = toString;
	}
	
	public String toString() {
		return this.toString;
	}
}
